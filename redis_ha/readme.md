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

<br/>

### Start the nodes and sentinels

For each node, open a terminal (window or pane), `cd` into it and run `./start.sh`:

-   `cd node1; ./start.sh`
-   `cd node2; ./start.sh`

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
