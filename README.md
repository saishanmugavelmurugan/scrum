# Scrum SIS Assignment
#### Swagger-Api docs
- Swaagger URL - http://localhost:9091/swagger-ui/index.html#/
- Copy and Download - http://localhost:9091/api-docs 
#### Retrospective Rest API Reference (Postman):
- Create Retrospective (Post)- http://localhost:9090/retrospective
- Search Retrospective (Get)- http://localhost:9091/retrospective/{date(yyyy-MM-dd)}
- Get Retrospective (Get)- http://localhost:9090/retrospective?page=<pageNumber>&pageSize=<pageSize>

#### Feedback Rest API Reference (PostMan):
- Create Feedback (Post)- http://localhost:9090/feedback
- Update Feedback (Put)- http://localhost:9090/feedback/update

#### How to Run Scrum SIS Assignment
###### - mvn clean install to generate app jar
- commandLine - java -jar target/scrum-0.0.1-SNAPSHOT.jar
- Maven - mvn spring-boot:run


