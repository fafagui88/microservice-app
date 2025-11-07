# 🚀 Project 4 — Microservices System

Microservices-based system built using **Spring Boot**, **Eureka Server**, **MySQL**, and **Docker Compose**.  
Each service runs independently and communicates via **Service Discovery (Eureka)**.

---

## 🧰 Tech Stack

| Category | Tool / Framework | Description |
|-----------|------------------|--------------|
| **Language** | Java 21 | Main backend language |
| **Framework** | Spring Boot 3.5.7 | Base framework for each microservice |
| **Service Discovery** | Netflix Eureka Server | Registers and locates all microservices |
| **Database** | MySQL 8.0 | Relational data storage |
| **ORM** | Spring Data JPA | Object-relational mapping |
| **Build Tool** | MVND (Maven Daemon) | Fast build system for Java |
| **Annotation Tool** | Lombok | Reduces boilerplate (getters, setters, constructors) |
| **Containerization** | Docker & Docker Compose | Runs all services in isolated containers |

---

## ⚙️ Architecture Overview

```bash
[ CLIENT / POSTMAN / FRONTEND ]
             │
             ▼
     [ EUREKA SERVER (8761) ]
             │
   ┌─────────┼──────────┬──────────┐
   ▼         ▼           ▼          ▼
PRODUCT   ORDER       HISTORIES    USERS
SERVICE   SERVICE     SERVICE      SERVICE

📁 Folder Structure
Folder / Container	Description	Port	Database
eureka-server/	Service registry for discovery	8761	-
products/	Product management service	8081	product_db
orders/	Order and transaction handling	8082	order_db
histories/	User activity and order history	8083	history_db
users/	User account management (optional)	8084	user_db
mysql/	Database container	3306	-


🧩 Microservices Overview
🟩 Eureka Server
Method	Endpoint	Description
GET	http://localhost:8761	View service registry dashboard

🟦 Product Service

Base URL: http://localhost:8081/api/products

Method	Endpoint	Description	Request Body
GET	/	Get all products	-
GET	/{id}	Get product by ID	-
POST	/	Add new product	{ "name": "Laptop", "price": 15000000, "stock": 10 }
PUT	/{id}	Update product	{ "name": "Laptop Pro", "price": 17000000 }
DELETE	/{id}	Delete product	-

🟨 Order Service

Base URL: http://localhost:8082/api/orders

Method	Endpoint	Description	Request Body
GET	/	Get all orders	-
GET	/{id}	Get order by ID	-
POST	/	Create new order	{ "userId": 1, "productId": 5, "quantity": 2 }
GET	/user/{userId}	Get orders for specific user	-

🟧 Histories Service

Base URL: http://localhost:8083/api/histories

Method	Endpoint	Description	Request Body
GET	/{userId}	Get all histories for a user	-
POST	/	Add new history record	{ "userId": 1, "action": "ORDER_CREATED", "timestamp": "2025-11-07T10:30:00Z" }

🟪 User Service

Base URL: http://localhost:8084/api/users

Method	Endpoint	Description	Request Body
GET	/	Get all users	-
GET	/{id}	Get user by ID	-
POST	/register	Register new user	{ "name": "Gumilar", "email": "gumilar@demo.com", "password": "123456" }
POST	/login	Authenticate user (JWT Token)	{ "email": "gumilar@demo.com", "password": "123456" }
