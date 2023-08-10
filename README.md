# reviews-backend

Backend for the reviews application. Other repositories:
<br />
[frontend](https://github.com/Iispar/reviews-frontend)
<br />
[api](https://github.com/Iispar/review-summary-API)

## About this project

!! For a detailed look at the whole project and also this backend please refer to the document in the frontend that was created for the college course. !!
<br />
This project is the backend for the reviews application. Its purpose is to receive calls from the frontend and act accordingly to the calls and also communicate with the API and database.
It is a Spring Boot application written in Java with Maven. The database is a MySQL database that is hosted in AWS.
The application takes all addition, update and deletion calls to entities from the frontend and also has a few unique get calls to receive whole pages cleaner. It applies all business logic and creates entities to be able
to communicate them to the database.
<br />
It has unit tests that cover 100% of the methods and also integration tests for all the API endpoints. Testing is done solely with JUnit and Spring MVC Test framework.
<br />

**Currently this backend is not deployed anywhere because it is still in development.**

## Prerequisites 
To run and test this you will need Java and Maven installed.
## configuration
This project has the application.properties file for the build hidden because it includes passwords and links and also the key pair for the ssh is hidden. If you don't have these please contact me at iiro.s.partanen@gmail.com.
When you have the file just add it to the main/resources where the application-test.properties are located you will also need to create a ssh pipe to the backend before running, this will be instructed in the file you received from me.
Then you will be able to run the application as instructed.

The tests **can** be run without this file.
## Running
### Build 
To run this application you will need to fulfill configuration and prerequisites.

After that you can run the application from the terminal with `mvn spring-boot:run` 
or from your IDE by running the root class shopBackendApplication.
### Test
To run the tests you just need to clone the repository and from the root directory at the terminal run `mvn test`. If you would like to run specific tests you can run them with the command
`mvn test -Dtest=”TestClassName#TestMethodName”` so for example `mvn test -Dtest="ItemServiceTest#getItemsForAccountWorksWithAsc"`. If you would like to you can run the tests from your IDE by running the classes/methods.

## Technologies
This backend uses Java with Maven. The backend is built using spring boot. Java was chosen for having already learned the language and wanting to learn Spring boot and also a "proper" backend language.
<br />
The Database is MySQL and is hosted in AWS. MySQL is one of the most popular options and also have knowledge of SQL but no proof of these skills was why I chose MySQL. AWS seems like a very popular skill 
with job listings, so it seemed fitting to learn a bit of it here as hosting was needed for the application.
