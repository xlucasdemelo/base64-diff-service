# Diff Service

Assessment Application for WAES

## Getting Started

### Prerequisites

This application uses maven for dependency management, it is mandatory to install it.

```
Java 8: https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html
Maven: https://maven.apache.org/download.cgi

```

### Running with maven

Clone the project

```
git clone https://github.com/xlucasdemelo/base64-diff-service.git
```

to start the application:

```
mvn spring-boot:run
```

### Running tests

Execute the following command:

```
mvn test
```

### Integration tests

Integration tests are under the class **DiffIntegrationTests.java** and can be executed using JUnit runner


### Sample requests

The application have the following endpoints:

```
POST: http://localhost:8080/v1/diff/{id}/left
POST: http://localhost:8080/v1/diff/{id}/right
GET: http://localhost:8080/v1/diff/{id}
```

1.Inserting a Right value for Diff with id 100:

```
curl --header "Content-Type: application/json" --request POST --data 'YWJjZA=='  http://localhost:8080/v1/diff/100/right
```

2.Inserting a Left value for Diff with id 100:

```
curl --header "Content-Type: application/json" --request POST --data 'YWJjZA=='  http://localhost:8080/v1/diff/100/left
```

3.Request a diff from the Diff with id 100:

```
curl http://localhost:8080/v1/diff/100
```


### Logs

I've implemented a custom log4j2 log appender to log the id of the DIFF for each operation, so we can keep track of the operations performed for each Diff object.
We can use this to integrate with a machine data application like Splunk and investigate operations for certain Diff id.


```

2020-02-09 17:38:16.400  INFO lucas-VPCEH3QFX --- [nio-8080-exec-2] c.l.w.d.s.DiffOffsetService              : diff-id:100 Saving DIFF... DiffOffset(id=100, leftDirection=YWJjZA==, rightDirection=null)
2020-02-09 17:38:20.836  INFO lucas-VPCEH3QFX --- [nio-8080-exec-3] c.l.w.d.s.DiffOffsetService              : diff-id:100 Saving DIFF... DiffOffset(id=100, leftDirection=YWJjZA==, rightDirection=YWXXZAXX)
2020-02-09 17:38:24.853  INFO lucas-VPCEH3QFX --- [nio-8080-exec-4] c.l.w.d.s.DiffOffsetService              : diff-id:100 Performing diff...
2020-02-09 17:38:24.858  INFO lucas-VPCEH3QFX --- [nio-8080-exec-4] c.l.w.d.d.DiffOffset                     : diff-id:100 Calculating offset for id: 100 
2020-02-09 17:38:24.859  INFO lucas-VPCEH3QFX --- [nio-8080-exec-4] c.l.w.d.d.DiffOffset                     : diff-id:100 Offset list of diff: 100 is DiffOffsetResponseDTO(reason=DIFFERENT_PAYLOADS, Offsets=[Offset(offset=2, length=2), Offset(offset=6, length=2)])

```

### Documentation

I am using Swagger2 to provide the documentation for the application, it could be accessed at: 

```
http://localhost:8080/swagger-ui.html#/
```
