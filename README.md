# fitness-studio-management

This application is used to manage a fitness studio.
<h3>Main application's functionalities:</h3>
<ul> 
    <li>Different CRUD operations for Users, Gym Events, Fitness classes and Trainers</li> 
    <li>Ability to assign/unassign a trainer to/from a fitness class</li> 
    <li>Ability to add the fitness class to a gym event</li> 
    <li>Registering a user in the system</li> 
    <li>Possibility for a logged user to participate (enroll/disenroll) in the gym events</li> 
</ul> 
<br>
<h3>Used technologies:</h3>
<ol>
  <li>Java</li>
  <li>Spring Boot, Spring Security, Spring Data JPA</li>
  <li>JUnit, AssertJ, Mockito</li>
  <li>MySQL</li>
  <li>Lombok, Swagger</li>
</ol>
<br> 
<h3>Testing flow proposal:</h3>
<ol>
  <li>Use an SQL script to create a database and tables in the MySQL environment</li>
  <li>Use an SQL script (sample-data) to create a sample data</li>
  <li>Fill in information in the application.properties file to configure the database environment <i>note: refers to username and password</i></li>
  <li>Start the application</li>
  <li>Test the application with the use of an API platform (e.g. Postman) and Swagger (http://localhost:8080/swagger-ui/index.html)</li>
  <li>The additional information about the API and its endpoints is available in Swagger's documentation</li>
</ol>
<br>
<h3>Description from Swagger's documentation.</h3>
<h4>IMPORTANT: To use different endpoints it may be necessary to log in</h4> 
<p>The application is secured with a JWT token. </p> 
<p>To obtain the <strong>access_token*</strong> it is recommended to use an API platform (e.g. Postman)</p> 
<h4>Request URL for login: localhost: http://localhost:8080/api/v1/login</h4> 
<p>Sample credentials after data creation by a CommandLineRunner </p> 
<p>{ username: "user", password: "password" } - role <strong>USER</strong></p> 
<p>{ username: "admin", password: "password" } - role <strong>ADMIN</strong></p> 
<h4>To Authorize and test the API's endpoints with granted permissions add: Bearer access_token<strong>*</strong></h4> 
<p>Requirements for the permissions are described in the notes of every request.<p>
