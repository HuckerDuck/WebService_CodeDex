# WebService_CodeDex

Backend web service for storing and retrieving **CodeMons** in **MongoDB**. 
Built with **Java + Spring Boot** and Gradle. 

> - This is a school project from STI and sends CodeMons to MongoDB.

## Overview 
This CodeDex program built inside Java Spring sends CodeMons to a MongoDB database. 
Then we use a Frontend to see the CodeMons inside the database and display them


## Project Structure
src/main/java/org/codedex/
  controller/   → Rest Controller that send to the database
  service/      → Uses most of the logic and the if cases that is sent to the controller
  model/        → Model is what we send to the database 
  repository/   → Uses a MongoDB repository
  DTO/ → Is used from model and then to the others. Uses alot of Valid commands
          that make use that the data enterd is correct, not empty and that for example to hp och the codemon isn't 0
src/main/resources/
  application.properties → Configuration (don't add this publicly)

## Tech Stack
- **Java** (21 is recommended)
- **Spring Boot** (Web, Data MongoDB)
- **Gradle** (Kotlin DSL)
- **MongoDB** (Atlas or local)

## Quick Start (IntelliJ & CLI)

### Prerequisites
- Java 21
- MongoDB connection (local or Atlas)
- Gradle Wrapper is included (`./gradlew`)

### Run in IntelliJ IDEA
1. Open the project folder in IntelliJ.
2. Let IntelliJ import the Gradle project.
3. Create `src/main/resources/application.properties` (see **Configuration**).
4. Run the application via the Gradle task **bootRun** or the main class run configuration.
5. API should be available on `http://localhost:8080`.

### Run via Terminal
```bash
# From project root
./gradlew bootRun

### Application properties file
spring.data.mongodb.uri=<your-mongodb-uri>
spring.data.mongodb.database=codex
server.port=8080  

Put that inside the application properties file then make sure that you put the enviromental
variables that you need localy and correct. Don't share them..

Typical resource: CodeMon

GET /api/codemon — list all CodeMons
GET /api/codemon/{id} — get by id
POST /api/codemon — create new CodeMon (JSON body)
PUT /api/codemon/{id} — update CodeMon
DELETE /api/codemon/{id} — remove CodeMon

Additional endpoints:

- **GET** `/codemons/filter/{category}/{value}` — list CodeMons filtered by category and value
- **GET** `/codemons/types` — list all CodeMon types, you can choose to filter by generation as well 
- **GET** `/codemons/after` — list CodeMons created after a given date  
- **GET** `/codemons/before` — list CodeMons created before a given date 
- **GET** `/codemons/generation/{gen}/top` — list CodeMons by generation, it's set to display top damage by default

## Future Improvements
- More Test and include WebFlux
- Add authentication/authorization (JWT)
- Add extra security maybe to the database
- Add docker to the database as well
- Add a way to use it will render

## License
All Rights Reserved.  
This project and its contents may not be copied, modified, distributed, or used in any form without explicit permission from the author.
