# Project Base for Master's Thesis: 
"Entwicklung eines verteilten Softwaresystems zur gemeinschaftlichen Erstellung von Dienstplänen nach dem Bottom-Up Ansatz".


Import the project to the IDE of your choosing as a Maven project. 

### Used Frameworks/Components
- Spring Boot
- Spring Security
- Spring Data
- Spring WebSocket
- MySQL Database
- Vaadin 10/11 Flow
- Polymer 2 Templates
- Material Design
- paper.js

### Running the Application

1. Start external services (DB, RabbitMQ message-broker) by running Docker Compose File
 `Docker/docker-compose.yml` through command `docker-compose up` in directory `Docker`

2. Run `bower install` in directory `src/main/webapp/frontend/` to fetch frontend-dependencies

3. Run application using `mvn spring-boot:run` or directly running Application class from your IDE

4. Open `http://localhost:8080/` in browser


For documentation on using Vaadin Flow and Spring, visit [vaadin.com/docs](https://vaadin.com/docs/v10/flow/spring/tutorial-spring-basic.html)

For more information on Vaadin Flow, visit https://vaadin.com/flow.


