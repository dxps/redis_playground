# A Spring Boot 3 using RedisOM example

This example showcases some of the features that [RedisOM for Java](https://redis.io/docs/latest/integrate/redisom-for-java/) provides.

<br/>

## Run

Use `mvn spring-boot:run` to start the app.

<br/>

## Usage

### Create items

This feature uses repositories' `save()` method.

-   Create a doc item using `curl -X POST http://localhost:8080/api/items -H 'Content-Type: application/json' --data '{ "name": "doc-1", "type": "doc" }'`
-   Create a hash item using `curl -X POST http://localhost:8080/api/items -H 'Content-Type: application/json' --data '{ "name": "hi 1", "type": "hash" }'`

### Get all items

This feature uses repositories' `findAll()` method.

-   Get All Items using `curl http://localhost:8080/api/items`
-   Get All Items of type doc using `curl http://localhost:8080/api/items?type=doc`
-   Get All Items of type hash using `curl http://localhost:8080/api/items?type=hash`

### Get item by id

This feature uses repositories' `findById()` method.

-   Get an item by id using `curl http://localhost:8080/api/items/0e4d8d23-017d-4dc4-b4d7-e403615536cc`

Obviously, after creating one or more items, get them all to find an id to use in this case.

### Search by name

This feature uses repositories' `findByName()` method.

-   Search doc items named 'doc-1' using `curl "http://localhost:8080/api/items/search?type=doc&name=doc-1"`
-   Search hash items named 'hi 1' using `curl "http://localhost:8080/api/items/search?type=hash&name=hi%201"`
