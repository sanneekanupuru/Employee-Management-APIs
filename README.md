# Employee Management APIs - Spring Boot

This is a simple Spring Boot project built to perform basic CRUD (Create, Read, Update, Delete) operations on Employee data. It also includes search functionality implemented in three ways (JPA Specifications, HQL/JPQL and Native SQL), basic validations, meaningful HTTP responses, unit tests (Mockito) and integration tests.

---

## 🧰 How to Run

### Option 1: Using IntelliJ (Recommended)
1. Open IntelliJ IDEA.
2. Click **File → Open** → select your project folder (the one containing `pom.xml`).
3. Run the main class **EmployeeApiApplication.java**.

### Option 2: Using Maven Command
Open a terminal in the project root folder and run:
```
mvn clean install
mvn spring-boot:run
```

### 🗄️ H2 Database Console

Access your in-memory H2 database at:

URL: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:employee-db

User: sa

Password: (leave blank)

### 📮 API Endpoints

Below are all 7 APIs implemented.
Each is tested and working properly with Postman.

### 1️⃣ GET — Fetch Employee by Email

Endpoint: GET /employees/email/{email}

Description: Fetch an employee’s details using their email.

Example: GET http://localhost:8080/employees/email/sannee@gmail.com

Response: 200 OK

{
"id": 1,
"firstName": "Sannee",
"lastName": "Challa",
"email": "sannee@gmail.com",
"phone": "9998887776",
"address": "Tirupati, AP, India"
}

### 2️⃣ GET — Fetch Employee by Name

Endpoint: GET /employees/name/{name}

Description: Fetch all employees whose first or last name matches the given name (case-insensitive).

Example: GET http://localhost:8080/employees/name/Sannee

Response: 200 OK

[
{
"id": 1,
"firstName": "Sannee",
"lastName": "Challa",
"email": "sannee@gmail.com",
"phone": "9998887776",
"address": "Tirupati, AP, India"
}
]

### 3️⃣ POST — Create Employee (name, email required)

Endpoint: POST /employees

Request Body (JSON):

{
"firstName": "Sannee",
"lastName": "Kanupuru",
"email": "sannee@gmail.com"
}

Response: 201 Created

{
"id": 1,
"firstName": "Sannee",
"lastName": "Kanupuru",
"email": "sannee@gmail.com",
"phone": null,
"address": null
}

Description: Creates a new employee record (firstName and email required).

### 4️⃣ POST — Create Employee with Details (name, email, phone required)

Endpoint: POST /employees/details

Request Body (JSON):

{
"firstName": "Bharath",
"lastName": "Kumar",
"email": "bharath@gmail.com",
"phone": "9876543210"
}

Response: 201 Created

{
"id": 2,
"firstName": "Bharath",
"lastName": "Kumar",
"email": "bharath@gmail.com",
"phone": "9876543210",
"address": null
}

Description: Creates a new employee record with name, email, and phone (all required except lastName).

### 5️⃣ PUT — Update Employee Details (lastName, phone, address)

Endpoint: PUT /employees/update

Request Body (JSON):

{
"email": "sannee@gmail.com",
"lastName": "Challa",
"phone": "9113322264",
"address": "Tirupati, AP, India"
}

Response: 200 OK

{
"id": 1,
"firstName": "Sannee",
"lastName": "Challa",
"email": "sannee@gmail.com",
"phone": "9113322264",
"address": "Tirupati, AP, India"
}

Description: Updates last name, phone, or address of the employee identified by their email.

### 6️⃣ PATCH — Update Only Phone by Email

Endpoint: PATCH /employees/phone/{email}

Request Body (JSON):

{
"phone": "9998887776"
}

Response: 200 OK

{
"id": 1,
"firstName": "Sannee",
"lastName": "Challa",
"email": "sannee@gmail.com",
"phone": "9998887776",
"address": "Tirupati, AP, India"
}

Description: Updates only the phone number of an employee by their email.

### 7️⃣ DELETE — Delete Employee by Email

Endpoint: DELETE /employees/email/{email}

Example: DELETE http://localhost:8080/employees/email/sannee@gmail.com

Description: Deletes the employee record matching the given email.

Response:

204 No Content — Deleted successfully

404 Not Found — No such employee found

### 🔍 Search Functionality (added — 6 endpoints)

Each search operation is implemented in three different approaches: JPA Specifications, HQL (JPQL), and Native SQL. Use any style you prefer; all return the same Employee JSON structure.

### A) JPA Specifications (dynamic queries)

### Search by email (JPA Specification):
GET /employees/search/spec/email/{email}

Example:

GET http://localhost:8080/employees/search/spec/email/sannee@gmail.com


Returns: 200 OK with employee JSON or 404 Not Found.

### Search by name (JPA Specification):
GET /employees/search/spec/name/{name}

Example:

GET http://localhost:8080/employees/search/spec/name/Sannee


Returns: 200 OK with list of employees or 404 Not Found.

### B) HQL / JPQL

### Search by email (HQL):
GET /employees/search/hql/email/{email}

Example:

GET http://localhost:8080/employees/search/hql/email/sannee@gmail.com


### Search by name (HQL):
GET /employees/search/hql/name/{name}

Example:

GET http://localhost:8080/employees/search/hql/name/Challa

### C) Native SQL

### Search by email (Native SQL):
GET /employees/search/native/email/{email}

Example:

GET http://localhost:8080/employees/search/native/email/sannee@gmail.com


### Search by name (Native SQL):
GET /employees/search/native/name/{name}

Example:

GET http://localhost:8080/employees/search/native/name/Sannee

### Notes for URLs

If an email contains @, Postman / curl / browser usually handle it fine. If not, URL-encode @ as %40, e.g. sannee%40gmail.com.

### 🧪 Tests (Unit + Integration)
Unit tests (Mockito + JUnit 5)

File: src/test/java/com/example/employeeapi/service/EmployeeServiceTest.java

Tests mock EmployeeRepository and verify service behavior (search methods, update behavior).

Run with:
```
mvn -Dtest=EmployeeServiceTest test
```

Integration tests (H2)

File: src/test/java/com/example/employeeapi/repository/EmployeeRepositoryIntegrationTest.java

Uses @DataJpaTest and in-memory H2 — verifies HQL, native queries and JPA Specifications against the database.

Run with:
```
mvn -Dtest=EmployeeRepositoryIntegrationTest test
```

Or run all tests:
```
mvn test
```
