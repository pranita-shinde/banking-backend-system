# 💳 Banking Backend System

A backend application simulating core banking operations, built using Java and Spring Boot following a layered architecture with secure authentication and real-world banking features.

---

## 🚀 Features
- Account Creation & Management  
- Deposit, Withdrawal & Fund Transfer  
- Secure Login with Username, Password & PIN  
- JWT-based Authentication & Authorization  
- Role-Based Access Control (USER / ADMIN)  
- Account Lock after multiple failed login attempts  
- Transaction Tracking (DEPOSIT, WITHDRAW, TRANSFER) with Remarks  
- Pagination for Transaction History  
- Global Exception Handling  
- Logging using SLF4J  

---

## 🏗️ Architecture
Controller → Service → Repository → Database  

- Clean separation of concerns  
- DTO-based API design (no entity exposure)  
- Layered architecture for scalability and maintainability  

---

## 🛠️ Tech Stack
- Java  
- Spring Boot  
- Spring Security (JWT)  
- REST APIs  
- MySQL  
- Maven  
- SLF4J Logging  

---

## 📂 Project Structure
src/main/java/com/hunt/demo/  
├── controller  
├── service  
├── repository  
├── dto  
├── entity  
├── exception  
├── mapper  
├── config  
├── filter  

---

## 🔗 API Endpoints (Sample)

| Method | Endpoint | Description |
|--------|---------|------------|
| POST | /accounts | Create account |
| GET | /accounts/{id} | Get account details |
| PUT | /accounts/{id}/deposit | Deposit money |
| PUT | /accounts/{id}/withdraw | Withdraw money |
| POST | /accounts/transfer | Transfer money |
| GET | /accounts/{id}/transactions | Get paginated transactions |
| POST | /auth/login | User login (JWT token) |
| GET | /admin/test | Admin-only access |
| PUT | /admin/unlock/{id} | Unlock user account |

---

## ▶️ How to Run

1. Clone the repository  
2. Open in Spring Tool Suite / IntelliJ  
3. Configure database in `application.properties`  
4. Run the application  
5. Use Postman to test APIs  
6. Add JWT token in Authorization header:  
   `Bearer <your_token>`  

---

## 📌 Highlights
- Implemented JWT-based authentication and role-based authorization  
- Designed secure APIs using Spring Security filters  
- Ensured transaction consistency using `@Transactional` (ACID principles)  
- Implemented pagination for efficient data retrieval  
- Added account lock mechanism for security enhancement  
- Used DTO pattern for clean and secure API responses  
- Integrated logging for debugging and monitoring  

---

## 👩‍💻 Author
**Pranita Shinde**
