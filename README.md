# JavaAdvancedCasestudy
Case Study for Java advanced


Implemented Microservices

Discovery Service 
Auth Service  - Takes care of login and authentication
Employee Service  - Takes care of employee related functionality

----------------------------------------------------------------------------------------------------------------------------------------------

Auth Service functionalities:

1) SignUp - Create User with loginId and password
2) login - Checks loginId and password and returns a session token (30 minutes validity) after which they are prompted to login again.
3) logout - Clears the token
4) validateToken - Endpoint which employee services call to check if the token provided is valid or not so as to allow employee related functionality.

Swagger available at endpoint - http://localhost:8083/swagger-ui/index.html#/

Separate DB - userinfo

----------------------------------------------------------------------------------------------------------------------------------------------

Employee Service functionalities

Before every API call in the employee service, there is a call made to Auth Service to check if the token is valid and then allowed to proceed with the functionality. 

1) Data Upload as CSV file - Reads employee record from CSV file and stores in the database
2) Fetch Particular Employee - Fetches all the information regarding a particular employee
3) Modify a Particular Employee - Modify the stored information regarding a particular employee 
4) MultiSelect Search based on criterias - Searches employee record based on multiple queries

    Queries supported
    
    EmployeeID
    FirstName like anything wildcard matching
    LastName like anything wildcard matching
    DateOfBirth - Greater than, Lesser than and Equal to a provided date in the search criteria
    DateOfJoining - Greater than, Lesser than and Equal to a provided date in the search criteria
    Grade - Equal to selected grade 

Implemented criteria query for search and also provided pagination support for the search results

5) Export the Search Results to CSV (Export CSV option) -  All the searched results would be available as CSV download file.

Separate DB - employeeinfo

Discovery Service for employee and auth service to communicate with each other

----------------------------------------------------------------------------------------------------------------------------------------------



