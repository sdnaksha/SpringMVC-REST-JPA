User management system:

User consists of:
	• First name
	• Last name
	• Phone number
	• Email (should be unique)
	• Date of birth

Service has the following APIs:
	• Retrieve a list of all users.
	• Add a user. If a user with this email already exists, an HTTP conflict response is returned.
	• Remove a user. Returns true if the user was successfully deleted, else false.
	• Find the average age, for all the users with the birthday between two given dates.

Tools used:
1. Language : Java
2. Framework: Spring MVC, using JPA to model user records in the database.
3. Database: H2 In-memory database.
4. Service Architecture: RESTful
5. Build: Maven

Instructions to run the service:

1. Unzip the contents of the zipped file.
2. Ensure jdk 1.8 and maven are installed on the machine where the project is run.
3. cd into EyeReturn folder.
4. Ensure you can see pom.xml in the current directory.
5. Build the project (runs test cases): mvn clean package
6. Run the service: java -jar target/EyeReturn-0.0.1.jar

The service will be up on your host (http://localhost:8080)