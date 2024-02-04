# reviews-backend

Backend for the reviews application. Other repositories:
<br />
[frontend](https://github.com/Iispar/reviews-frontend)
<br />
[api](https://github.com/Iispar/review-summary-API)

## About this project

**!! For a detailed look at the whole project and also this backend please refer to the document in the frontend that was created for the college course. !!**

This project is the backend for the reviews application. Its purpose is to receive calls from the frontend, act according to the calls, and communicate with the API and database.
It is a Spring Boot application written in Java with Maven. The database is a MySQL database that is hosted in AWS.
The application takes all addition, update and deletion calls to entities from the frontend and also has a few unique get calls to receive whole pages cleaner. It applies all business logic and creates entities to be able
to communicate them to the database.


It has unit tests that cover 100% of the methods and integration tests for all the API endpoints. Testing is done solely with JUnit and Spring MVC Test framework.
<br />

This is deployed on Render.com

## Prerequisites 
To run and test you will need Java and Maven installed.
## configuration
This project has the application.properties file for the build hidden because it includes passwords and links. If you don't have these please contact me at iiro.s.partanen@gmail.com. When you have the file just add it to the main/resources where the application-test.properties are located.
<br />
<br />
The tests **can** be run without these files.
## Running
### Build 
To run this application you will need to fulfill configuration and prerequisites.

If you would like to use docker you need to have it installed and then run from the folder that includes the dockerfile
```docker build --tag backend .```. After this just run ```docker run -p 8080:8080 backend``` and the application should start at 
localhost:8080

To run in the command line you need to first run a SSH tunnel that is described in the application.properties file.


As the target files are in the GitHub you can also just run the .jar file from the root folder with ```java -jar target/reviewsbackend-0.0.1-SNAPSHOT.jar```

If you want to compile it yourself just run ```mvn install``` and then the previous command. If you want to run the application yourself run ```mvn clean install``` and ```mvn spring-boot:run``` and the application should start. If you want to run from the IDE run first the ```mvn clean install``` and then run the root class shopBackendApplication in your IDE


### Test
To run the tests you just need to clone the repository and from the root directory at the terminal run `mvn test`. If you would like to run specific tests you can run them with the command
`mvn test -Dtest=”TestClassName#TestMethodName”` so for example `mvn test -Dtest="ItemServiceTest#getItemsForAccountWorksWithAsc"`. You can also run the tests from your IDE by running the classes/methods.

## Technologies
This backend uses Java with Maven. The backend is built using spring boot. Java was chosen for having already learned the language and wanting to learn Spring boot and also a "proper" backend language.


The Database is using MySQL and is hosted in AWS. MySQL is one of the most popular options and also have knowledge of SQL but no proof of these skills was why I chose MySQL. AWS seems like a very popular skill 
with job listings, so it seemed fitting to learn a bit of it here as hosting was needed for the application.
