## Redis High Availability

_High Availability_ feature is offered based on database replication.
This means having a primary (aka master) and one or more replica (aka slave) nodes with:

-   primary node:
    -   handling both write and read requests
    -   delivering the changes to replica node(s)
-   replica node(s):
    -   handling the read requests
    -   receiving and applying changes locally

Note that this is different than _Cluster_ setup, which implies a data sharding approach, where multiple masters exist, each being responsible with a part of the data. For further details, see [Durability and high availability](https://redis.io/docs/latest/operate/rs/databases/durability-ha/) page.

For this setup, Redis [Sentinel](https://redis.io/docs/latest/operate/oss_and_stack/management/sentinel/) is needed. And here is the deployment overview in this case:

```
       ╭──────────────╮         ╭─────────────╮
       │    Redis     ├─────────▶    Redis    │
       │   (master)   │         │   (slave)   ◀ ─ ─ ─ ─ ─ ─ ─
       ╰────▲─────────╯         ╰─────────────╯              │
            │
            │                                                │
        ╭───┴─────────────┬──────────────────╮
        │                 │                  │               │
        │                 │                  │
╭───────┴──────╮   ╭──────┴───────╮   ╭──────┴───────╮       │
│              │   │              │   │              │
│  Sentinel 1  │   │  Sentinel 2  │   │  Sentinel 3  │       │
│              │   │              │   │              │
╰───────┬──────╯   ╰──────┬───────╯   ╰──────┬───────╯       │

        │                 │                  │               │
         ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  ─ ─ ─ ─ ─ ─ ─ ─
```

Here are the initial configuration files for the Redis nodes and the sentinels, before getting updated at their respective startup:

-   node1's `redis.conf`:
    ```
    appendonly yes
    min-replicas-to-write 1
    min-replicas-max-lag 10
    port 7001
    ```
-   node2's `redis.conf`:

    ```
    appendonly yes
    min-replicas-to-write 1
    min-replicas-max-lag 10
    port 7002
    replicaof 127.0.0.1 7001
    ```

-   node3's `redis.conf`:
    ```
    appendonly yes
    min-replicas-to-write 1
    min-replicas-max-lag 10
    port 7003
    replicaof 127.0.0.1 7001
    ```
-   sentinel1's `sentinel.conf`:
    ```
    port 5001
    sentinel monitor myMaster 127.0.0.1 7001 2
    sentinel down-after-milliseconds myMaster 5000
    sentinel failover-timeout myMaster 60000
    ```
-   sentinel2's `sentinel.conf`:
    ```
    port 5002
    sentinel monitor myMaster 127.0.0.1 7001 2
    sentinel down-after-milliseconds myMaster 5000
    sentinel failover-timeout myMaster 60000
    ```
-   sentinel3's `sentinel.conf`:
    ```
    port 5003
    sentinel monitor myMaster 127.0.0.1 7001 2
    sentinel down-after-milliseconds myMaster 5000
    sentinel failover-timeout myMaster 60000
    ```

<br/>

### Start the nodes and the sentinels

For each node, open a terminal (window or pane), `cd` into it and run `./start.sh`:

-   `cd node1; ./start.sh`
-   `cd node2; ./start.sh`
-   `cd node3; ./start.sh`

Check the replication state on any of the nodes.
For example, node1 should report as being the master with one connected slave:

```shell
❯ redis-cli -p 7001
127.0.0.1:7001>
127.0.0.1:7001> info replication
# Replication
role:master
connected_slaves:1
min_slaves_good_slaves:1
slave0:ip=127.0.0.1,port=7002,state=online,offset=14,lag=1
master_failover_state:no-failover
master_replid:3fd1dab495adf9903f9f7435c4f79e76e42576a7
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:14
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:14
127.0.0.1:7001>
```

Same as before, for each sentinel, open a terminal (window or pane), `cd` into it and run `./start.sh`:

-   `cd sentinel1; ./start.sh`
-   `cd sentinel2; ./start.sh`
-   `cd sentinel3; ./start.sh`

All sentinels are connecting to the master (1st) Redis node and:

1. The slave(s) are discovered automatically.
2. Their own configuration (`sentinel.conf`) file gets updated.

### Tests

1. Set a key in master and see that it is replicated on the slave.\
    - On master:
        ```
        127.0.0.1:7001> set k1 val1
        OK
        127.0.0.1:7001>
        ```
    - On a slave:
        ```
        127.0.0.1:7002> get k1
        "val1"
        127.0.0.1:7002>
        ```
2. Kill the master.\n
    - Find the redis servers process ids using `./list_redis_ps.sh`.
        ```shell
        ❯ ./list_redis_ps.sh
        dxps     1613884 1613883  0 17:51 pts/20   00:00:00 redis-server *:7001
        dxps     1613929 1613928  0 17:51 pts/21   00:00:00 redis-server *:7002
        dxps     1614116 1614115  0 17:51 pts/7    00:00:00 redis-server *:7003
        ❯
        ```
    - Kill the process that is running `redis-server *:7001`.
        ```shell
        ❯ kill -9 1613884
        ❯
        ```
3. Verify the result:
    1. The replica nodes would report _Error condition on socket for SYNC: Connection refused_ entries.
    2. The sentinels would discover the master outage, start the voting for the leader, and promote the replica as master. See their output for details.
    3. Connect to a sentinel and see the state:
        ```shell
        ❯ redis-cli -p 5001
        127.0.0.1:5001>
        127.0.0.1:5001> sentinel master myMaster
        1) "name"
        2) "myMaster"
        3) "ip"
        4) "127.0.0.1"
        5) "port"
        6) "7003"
        ```
    4. Connect to the newly promoted master node and get the replication state:
        ```
        127.0.0.1:7003> info replication
        # Replication
        role:master
        connected_slaves:1
        min_slaves_good_slaves:1
        slave0:ip=127.0.0.1,port=7002,state=online,offset=55464,lag=0
        master_failover_state:no-failover
        master_replid:909656b26b23ff8e9d07560c71342d87255700ba
        master_replid2:166fe2cf67a56a22a74c9261a724abde2c00f6cb
        master_repl_offset:55596
        second_repl_offset:46396
        127.0.0.1:7003>
        ```
    5. Connect to the (only remaining) slave node and get the replication state:
        ```shell
        ❯ redis-cli -p 7002
        127.0.0.1:7002>
        127.0.0.1:7002> info replication
        # Replication
        role:slave
        master_host:127.0.0.1
        master_port:7003
        master_link_status:up
        master_last_io_seconds_ago:1
        master_sync_in_progress:0
        slave_read_repl_offset:71812
        slave_repl_offset:71812
        slave_priority:100
        slave_read_only:1
        replica_announced:1
        connected_slaves:0
        min_slaves_good_slaves:0
        master_failover_state:no-failover
        master_replid:909656b26b23ff8e9d07560c71342d87255700ba
        master_replid2:166fe2cf67a56a22a74c9261a724abde2c00f6cb
        master_repl_offset:71812
        second_repl_offset:46396
        ```
