# Employee Management System - Spring Boot & React & My SQL

## Project Overview
The Employee Management System is a full-stack web application designed to manage employee and project information. It is built using Spring Boot for the back-end and React for the front-end, with MySQL as the database layer. This application supports CRUD operations for managing employees and projects and features user authentication and authorization.

## Features
- **User Authentication**: Secure login and logout functionality.
- **Employee Management**: Add, update, and remove employee information.
- **Project Management**: Assign employees to projects and track project progress.
- **Many-to-Many Relationship Management**: When an employee or project is updated, the changes are reflected in all associated entities. 
- **Search and Filter**: Search employees and projects by name with dynamic filtering.
- **Bulk Delete**: Allows for the bulk deletion of employees and projects.
- **Dashboard**: Managing and visualizing all key aspects.
- **Success&Error Message**:Displayed when an action is completed.

## ⚙️ Technologies Used

### Front-end
- **React** (Functional components, hooks)
- **Material-UI** (for UI components)
- **Axios** (for HTTP requests)
  
### Back-end
- **Spring Boot** (RESTful API development)
- **Spring Security** (Authentication and Authorization)
  
### Database
- **MySQL** (Relational database for storing employee and project data)

### npm Requirements
- **nmp axios** 
- **npm react-router-dom**
- **@mui/material**
- **@mui/icons-material**
- **@mui/x-data-grid**

### Other Software Tools
- **MySQLWorkbench** (Store user information)
- **Swagger** (Test PUT, GET, POST and DELETE requests)
  
## 📦 Installation Guide

### Prerequisites
- **Java 17+**
- **Node.js** (v16+)
- **MySQL**
  
### Steps to Set Up the Back-End (Spring Boot)
1. Clone the repository:  
   ```bash
   git clone https://github.com/Kyoka-run/EmployeeManagement-Project.git

2. Set up the MySQL database:
   Open MySQL and create a new schema
   ```bash
   CREATE DATABASE employee_management;
   Update the application.properties file with your MySQL credentials:
   ```bash
   spring.datasource.url=jdbc:mysql://localhost:3306/employee_management
   spring.datasource.username=YOUR_MYSQL_USERNAME
   spring.datasource.password=YOUR_MYSQL_PASSWORD
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

3. Run the application

### Steps to Set Up the Front-End (React)

1. Install dependencies:
    Navigate to the frontend directory and install dependencies:
    ```bash
    cd frontend
    npm install

2. Run the front-end application:
    Start the React development server:
    ```bash
    npm start

The application will be accessible at http://localhost:3000.

##  Backend Test Coverage

The Employee Management System project includes implemented test coverage to ensure that the application functions as expected and to reduce the risk of defects.  You can run the tests using the following commands:
    ```bash
    # Run tests
    ./mvnw test

    # Generate coverage report
    ./mvnw test jacoco:report
    
##  Swagger API Tests
![swagger](https://github.com/user-attachments/assets/8888cb83-6899-4910-b1ce-c6be837fb82e)

##  Application Screenshots

### Login Page 
![login](https://github.com/user-attachments/assets/953356be-0021-42a1-b525-0a9098eceded)
### Logout Page 
![logout](https://github.com/user-attachments/assets/ae51b60b-21b2-47c2-a19b-cb995e45b874)
### Register Page 
![register](https://github.com/user-attachments/assets/8afd65a6-3db1-43db-850b-fbe3d49c6118)
### Employee List Page 
![employee list](https://github.com/user-attachments/assets/09d52193-7fbb-455a-a6c9-f4114d99f854)
### Project List Page 
![project list](https://github.com/user-attachments/assets/1750b88d-7c16-4bf2-a296-788b42d50acc)
### Dashboard Page 
![dashboard](https://github.com/user-attachments/assets/ccd6ea91-701f-46c8-ab91-34f253b104aa)
### Update Employee Page 
![update employee](https://github.com/user-attachments/assets/00a83f51-828e-40f2-80c9-9e0b7437095b)
### Create Project Page 
![create project](https://github.com/user-attachments/assets/5122b5b1-9748-4d30-bb7f-7b0cd4cff0da)
### Serach Function Page 
![serach function](https://github.com/user-attachments/assets/c7c23662-7fc3-4eee-a600-df2880793d0d)
### Bulk Delete Page 
![bulk delete](https://github.com/user-attachments/assets/909b8bb7-ca4a-47fe-a5cd-d9e30b52efd0)
