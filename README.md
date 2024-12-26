# Employee Management System - Spring Boot & React & MySQL

## 🎯 Project Overview
The Employee Management System is a comprehensive full-stack web application for managing employees and projects, featuring secure user authentication, role-based authorization, and real-time data management. Built with Spring Boot, React, and MySQL.

The project consists of two main repositories:
- Backend Repository: [`EmployeeManagement-Backend`][backend]
- Frontend Repository: [`EmployeeManagement-Frontend`][frontend]

The application is deployed and accessible through AWS:
[`Live Demo`][demo]

[backend]: https://github.com/Kyoka-run/EmployeeManagement-Backend
[frontend]: https://github.com/Kyoka-run/EmployeeManagement-Frontend  
[demo]: http://employee-management-frontend-kyoka.s3-website-eu-west-1.amazonaws.com

## ✨ Key Features

### User Authentication & Authorization
- **JWT-based authentication**
- **Role-based access control (ADMIN, EMPLOYEE_MANAGER, PROJECT_MANAGER, GUEST)**
- **Secure password hashing with BCrypt**

### Employee & Project Management
- **CRUD operations**
- **Employee & Project details**
- **Bulk operations**
- **Real-time search and filtering**

### Advanced Features
- **Responsive Material-UI interface**
- **Error handling and validation**
- **Success/Error notifications**

## ⚙️ Technologies Stack

### Back-end
- **Framework:** Spring Boot 2.7.2
- **Security:** Spring Security with JWT
- **Database:** MySQL with JPA/Hibernate
- **API Documentation:** Springdoc OpenAPI (Swagger)
- **Testing:** JUnit, Mockito
- **Build Tool:** Maven

### Front-end
- **Framework:** React (Functional components with Hooks)
- **UI Components:** Material-UI (MUI)
- **HTTP Client:** Axios
- **Form Handling:** Formik
- **Testing:** React Testing Library, Jest
  
### Development
- **Containerization:** Docker
- **Version Control:** Git
- **API Testing:** Swagger UI
- **CI/CD:** Jenkins

### Architecture
![Flowchart](https://github.com/user-attachments/assets/768dadec-9474-4386-b9b9-d9f037e0bacb)


## 🔍 Code Quality & Test Coverage

### Backend Test Coverage Results
![backend-coverage](https://github.com/user-attachments/assets/b6a79c53-99e0-407c-8d87-de87fbbf198b)

### Frontend Test Coverage Results 
![frontend-coverage](https://github.com/user-attachments/assets/90910b80-08c4-4681-8a31-9006e7c67c5f)

  
## 📊 Swagger API Tests

### API Documentation Access
- **Local Environment**: Access Swagger UI at `http://localhost:8080/swagger-ui.html`
![api document](https://github.com/user-attachments/assets/cff3d1b7-5c95-470f-8114-d95d166eebb2)


## 📦 Installation & Setup

### Prerequisites
- Java 17+
- Node.js 16+
- MySQL 8.0+
  
### Backend Setup
1. Clone the repository:
```bash
git clone https://github.com/Kyoka-run/EmployeeManagement-Project.git
```

2. Set up the MySQL database:
```sql
CREATE DATABASE employee_management;
```

3. Update the application.properties file with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_management
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

4. Run the application

### Frontend Setup

1. Install dependencies:
```bash
cd frontend
npm install
```

2. Configure API endpoint:
```javascript
// Update API_URL in service files if needed
const API_URL = 'http://localhost:8080';
```

3. Start development server:
```bash
npm start
```

## 🧪 Testing

### Back-end
```bash
# Run all tests
mvn test

# Generate coverage report
mvn jacoco:report
```

### Front-end
```bash
# Run all tests
npm test

# Run with coverage
npm test -- --coverage

# Run specific test file
npm test -- LoginComponent.test.js
```

## 🛠 Application Screenshots

### Login Page 
![login](https://github.com/user-attachments/assets/44b5ca1b-1627-4859-8ea7-c5a92174438f)

### Employee List Page 
![employeelist](https://github.com/user-attachments/assets/d5716003-d147-45d7-943a-fd8c31830537)

### Project List Page 
![projectlist](https://github.com/user-attachments/assets/d95731fe-984a-4186-a6c0-46a2bec6ec39)

### Dashboard Page 
![menu](https://github.com/user-attachments/assets/b3253e9d-db44-432d-bad2-20c78f5b2bd2)

### Update/Add Employee Page 
![updateemployee](https://github.com/user-attachments/assets/07013728-a620-428b-9e85-7f71f64e973a)

### Employee Detail Page 
![employeedetail](https://github.com/user-attachments/assets/c9eaf8ee-4716-4ed1-a211-11924e12c7a1)

### Change Password Page 
![changepassword](https://github.com/user-attachments/assets/5c03fed3-2f63-423e-a567-23740dc12e0d)





