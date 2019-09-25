# Video Statistics
Restful API for generating video statistics. The implemented use case is to calculate in real time the statistics of the last 60 seconds.

## Build & run

Run using Spring Boot
```
$ mvn spring-boot:run
```
Build
```
$ mvn clean package
```
Run
```
$ java -jar target\video-statistics-0.0.1-SNAPSHOT.jar
```

### API documentation

The following documentation is available while the service is running.

API: api.adoc  
[http://localhost:8080/docs/api.html](http://localhost:8080/docs/api.html)  
  
Swagger Test Tools  
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Stack
* Java 11
* [Spring boot 2.1.8](https://projects.spring.io/spring-boot/)
* [Maven](https://maven.apache.org/) - Dependency Management
* Lombok
* Spring Boot Test + Mockito + Restdocs/asciidoctor

## Authors

* **Thiago Rocha**
