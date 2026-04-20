# 💳 Banking Backend System

A backend application simulating core banking operations, built using Java and Spring Boot following a layered architecture.

---

## 🚀 Features
- Account Creation & Management  
- Deposit & Withdrawal Operations  
- Secure Login with Username, Password & PIN  
- Transaction Tracking with Timestamp  
- Exception Handling (Global)  
- Logging using SLF4J  

---

## 🏗️ Architecture
Controller → Service → Repository → Database  

- Clean separation of concerns  
- DTO-based API design (no entity exposure)  

---

## 🛠️ Tech Stack
- Java  
- Spring Boot  
- REST APIs  
- MySQL  
- Maven  
- SLF4J Logging  

---

## 📂 Project Structure
src/main/java/com/hunt/demo/ ├── controller ├── service ├── repository ├── dto ├── entity ├── exception ├── mapper
---

## 🔗 API Endpoints (Sample)

| Method | Endpoint | Description |
|--------|---------|------------|
| POST | /accounts | Create account |
| GET | /accounts/{id} | Get account details |
| PUT | /accounts/{id}/deposit | Deposit money |
| PUT | /accounts/{id}/withdraw | Withdraw money |
| POST | /auth/login | User login |

---

## ▶️ How to Run

1. Clone the repository  
2. Open in Spring Tool Suite / IntelliJ  
3. Configure database in `application.properties`  
4. Run the application  
5. Test APIs using Postman  

---

## 📌 Highlights
- Implemented DTO pattern for clean API design  
- Added database constraints (unique username, non-null fields)  
- Integrated logging for better debugging  
- Structured code using industry-standard practices  

---

## 👩‍💻 Author
**Pranita Shinde**
