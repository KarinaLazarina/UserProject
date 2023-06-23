### Build and Run Instructions
1) To build a project, open terminal and run following command: ***mvn clean package***
2) Next step is creating image, you need to run command: ***docker-compose build***
3) To run application and a MongoDB database use next command: ***docker-compose up***


#### Any important decisions or assumptions
- I decided to make the field "email" in the entity User unique because to my mind any application can have only one user with one email.
- I created UserDTO class to rule out the possibility of adding a custom id.

#### Brief explanation of applied clean code principles in project.

- On the service layer in the method updateUser I reuse the method getUserById to avoid duplication of check if the user exists (DRY)
- I created the method saveToDB because creating and updating logic is similar, to avoid code duplication (DRY)
- I divided solutions into several classes(UserService, UserRepository, UserController, etc.), and each class is responsible for its task (Single Responsibility Principle)
