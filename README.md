# SwiftCart Backend

SwiftCart is a **Multi-Vendor E-Commerce Web Application** built using Java and Spring Boot.

This repository contains the backend REST API responsible for business logic, authentication and authorization, database operations, product and order workflows, seller and administrator functionality, email/OTP functionality, and payment integration.

---

## 📌 Project Overview

The SwiftCart backend follows a REST-based architecture and provides APIs consumed by the React/TypeScript frontend.

The backend is responsible for:

* Processing client requests
* Validating request data
* Executing business logic
* Managing authentication and authorization
* Interacting with the MySQL database
* Processing orders
* Managing seller/customer operations
* Integrating payment providers
* Providing email/OTP functionality

---

## ✨ Features

### Authentication & Security

* User authentication
* JWT-based authentication
* Spring Security integration
* Role-based authorization
* Protected API endpoints
* JWT request filtering

### Customer Functionality

* User/profile management
* Product browsing and management APIs
* Product reviews
* Cart operations
* Wishlist operations
* Order operations
* Checkout-related processing

### Seller Functionality

* Seller registration/workflow
* Seller profile
* Seller dashboard operations
* Seller product management

### Administrator Functionality

* Administrator operations
* Administrative management APIs

### Payment

SwiftCart integrates:

* **Razorpay**
* **Stripe**

### Email

Spring Boot Mail is used for email-based functionality, including OTP-related operations.

---

## 🛠️ Technology Stack

| Technology         | Purpose                         |
| ------------------ | ------------------------------- |
| Java               | Backend programming language    |
| Spring Boot 3.5.16 | Backend framework               |
| Spring Web         | REST API development            |
| Spring Data JPA    | Database persistence            |
| Spring Security    | Security and authorization      |
| JWT                | Stateless authentication        |
| MySQL              | Relational database             |
| Gradle             | Build and dependency management |
| Lombok             | Reducing boilerplate code       |
| JJWT               | JWT implementation              |
| Razorpay Java SDK  | Razorpay integration            |
| Stripe Java SDK    | Stripe integration              |
| Spring Boot Mail   | Email/OTP functionality         |
| Spring Validation  | Request validation              |

---

## 🏗️ Backend Architecture

The backend follows a layered architecture.

```text
                 React Frontend
                       |
                       | HTTP / REST
                       v
                Spring Controllers
                       |
                       v
                  Service Layer
                       |
                       v
                Repository Layer
                       |
                       v
                 MySQL Database
```

For authenticated requests, security processing occurs before the request reaches protected application functionality.

```text
Client
  |
  | HTTP Request + JWT
  v
Spring Security
  |
  v
JWT Authentication Filter
  |
  v
Controller
  |
  v
Service
  |
  v
Repository
  |
  v
MySQL
```

---

## 🔐 Security & Authentication

SwiftCart uses **Spring Security** together with **JSON Web Tokens (JWT)**.

JWT provides stateless authentication between the frontend and backend.

The general authentication flow is:

```text
User Login
    |
    v
Authentication API
    |
    v
Credentials Validated
    |
    v
JWT Generated
    |
    v
Frontend Stores JWT
    |
    v
JWT Sent With Protected Requests
    |
    v
JWT Validated By Backend
    |
    v
Protected Resource Accessed
```

The backend also supports role-based access for different application roles.

Sensitive values such as:

* Database credentials
* JWT secrets
* Payment credentials
* Third-party service credentials

should be supplied through environment-specific configuration and should not be committed to a public repository.

---

## 🗄️ Database

SwiftCart uses **MySQL** as its relational database.

**Spring Data JPA** provides the persistence layer between the Java application and MySQL.

The database layer supports the application's e-commerce data, including information associated with:

* Users
* Products
* Categories
* Reviews
* Cart
* Wishlist
* Orders
* Sellers
* Other application workflows

---

## 🌐 REST APIs

The backend exposes REST APIs consumed by the frontend.

The API layer provides functionality for:

* Authentication
* User management
* Products
* Categories
* Reviews
* Cart
* Wishlist
* Orders
* Seller operations
* Administrator operations
* Payments
* Email/OTP functionality

The API is secured according to the authentication and authorization requirements of each operation.

---

## 💳 Payment Integration

SwiftCart supports two payment providers.

### Razorpay

The Razorpay Java SDK is used to support Razorpay payment processing.

### Stripe

The Stripe Java SDK is used to support Stripe payment processing.

The payment integrations are handled by the backend so that payment-related business logic and credentials are not exposed directly through the frontend.

---

## 📧 Email and OTP

SwiftCart uses **Spring Boot Mail** for email functionality.

The backend handles OTP-related processing and email delivery, keeping the verification logic on the server side.

---

## ✅ Request Validation

Spring Boot Validation is included in the backend to validate incoming request data.

Validation helps prevent invalid or incomplete data from being passed into the application's business and persistence layers.

---

## 📦 Build Configuration

The project uses **Gradle** for dependency management and builds.

The current project configuration includes:

```text
Spring Boot: 3.5.16
Java Toolchain: 25
Gradle: Gradle Wrapper / Gradle build system
Group: com.swiftcart
```

---

## 📚 Major Dependencies

The backend includes the following major Spring Boot modules:

```text
spring-boot-starter-web
spring-boot-starter-data-jpa
spring-boot-starter-security
spring-boot-starter-validation
spring-boot-starter-mail
```

Additional integrations include:

```text
JJWT
MySQL Connector/J
Razorpay Java SDK
Stripe Java SDK
Lombok
```

---

## ⚙️ Installation

### Prerequisites

Install:

* Java 25
* Git
* MySQL
* Gradle or use the included Gradle Wrapper

### Clone the Repository

```bash
git clone https://github.com/Ayu198/SwiftCart-backend.git
```

Move into the project directory:

```bash
cd SwiftCart-backend
```

---

## 🗄️ Database Configuration

Create a MySQL database for the application.

Configure the required database connection details in your local environment configuration.

Do not expose or commit sensitive credentials such as:

```text
Database password
JWT secret
Razorpay keys
Stripe keys
Email credentials
Cloud service credentials
```

---

## ▶️ Running the Backend

### Windows

```bash
gradlew.bat bootRun
```

### Linux / macOS

```bash
./gradlew bootRun
```

The Spring Boot application will start using the configured application settings.

---

## 🧪 API Testing

The REST APIs can be tested independently using **Postman**.

A typical testing workflow is:

```text
Postman
   |
   v
REST API Request
   |
   v
Spring Boot Controller
   |
   v
Service Layer
   |
   v
Database / External Service
   |
   v
API Response
```

This allows backend endpoints to be verified independently from the React frontend.

---

## 🧰 Development Tools

The project can be developed and tested using:

* IntelliJ IDEA
* Visual Studio Code
* Postman
* MySQL
* Git
* GitHub

---

## 🔗 Frontend Repository

The corresponding SwiftCart frontend is available here:

```text
https://github.com/Ayu198/SwiftCart-Frontend
```

---

## 🔄 Complete Application Flow

The complete SwiftCart architecture can be represented as:

```text
                  SwiftCart Frontend
                         |
                         | REST API
                         v
                Spring Boot Backend
                         |
             +-----------+-----------+
             |           |           |
          Security    Business    Validation
             |         Logic          |
             +-----------+-----------+
                         |
                         v
                   JPA Repository
                         |
                         v
                     MySQL DB
                         
External Integrations:
    ├── Razorpay
    ├── Stripe
    └── Email / OTP
```

---

## 📌 Project Information

**Project:** SwiftCart
**Project Type:** Multi-Vendor E-Commerce Web Application
**Backend:** Java + Spring Boot
**Security:** Spring Security + JWT
**Database:** MySQL
**ORM/Persistence:** Spring Data JPA
**Build Tool:** Gradle
**API Style:** REST

---

## 👨‍💻 Repository

```text
https://github.com/Ayu198/SwiftCart-backend
```
