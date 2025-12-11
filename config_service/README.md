# config-service (server_demo)

## Prerequisites
- Java 17
- Maven 3.8+
- Local config repo: create a folder `~/server_demo-config-repo` with files:
  - application.properties
  - auth-service.properties
  - task-service.properties
  etc.
  (You can initialize it as a git repo if you want to use git backend.)

## Run locally
1. Build:
   mvn -U -DskipTests package

2. Run:
   mvn spring-boot:run -Dspring-boot.run.profiles=local

The Config Server will start by default on port 8888.
Management endpoints available on 8889.

## Notes
- Client services should set: spring.cloud.config.uri=http://localhost:8888
- Default dev credentials: set in .env file

