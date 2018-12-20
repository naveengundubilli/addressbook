# addressbook

Enviroment Details:

jdk 1.8.192
Eclipse Oxygen
Tomcat 8.0.35
Maven -apache-maven-3.3.3 and above
JUnit 4 and above

1)Checkout the current branch and create a workspace in eclispe and setup the build path entries
2)Run /addressbook-rest-service/src/main/java/code/restful/AddressBookApplication.java as a java application

Use the below url to see the initial data and also new entries after operations
http://localhost:8080/addressbook/contactdetails

To do operations and test the application please use the below url 
http://localhost:8080/swagger-ui.html to see all the operations in the rest api

Unit Testing

/addressbook-rest-service/src/test/java/test/restful/addressbook/tests/MockServerTests.java as JUnit TestCase
