# 🎬 Movie Ticket Booking System

A robust, enterprise-grade REST API built with **Spring Boot** that demonstrates how to solve the classic "Race Condition" (double-booking) problem in high-traffic ticketing systems using **Pessimistic Locking**.

## 🚀 Live Demo & Links

* **Live Application (Render):** `https://your-app-name.onrender.com` *(Replace this link once Render deployment finishes)*
* **Database (Neon Cloud):** Serverless PostgreSQL hosted on [Neon.tech](https://neon.tech)

## 🛠️ Tech Stack
* **Java 17** & **Spring Boot 3**
* **Spring Data JPA / Hibernate**
* **Spring Security** (Basic Auth & DB-backed custom users)
* **PostgreSQL** (Neon Cloud)
* **Docker** (For Render deployment)

## 🧠 The Race Condition Solution
If User A and User B both attempt to book the exact same seat at the exact same millisecond, a standard `if(seat == AVAILABLE)` check will fail, leading to a double-booked seat. 

**How this project solves it:**
* **Pessimistic Database Lock:** The repository uses `@Lock(LockModeType.PESSIMISTIC_WRITE)` (`SELECT ... FOR UPDATE`), physically locking the row in PostgreSQL so the second thread is forced to wait until the first completes.

## 🏃‍♂️ How to Run Locally

1. Clone the repository.
2. Ensure you have a local Redis server running on port `6379`.
3. Update `application.properties` with your Neon PostgreSQL JDBC URL.
4. Run the application:
```bash
mvn spring-boot:run
```

## 🔒 Test Credentials
The database automatically seeds with two test users:
* **Username:** `alice` | **Password:** `password`
* **Username:** `bob` | **Password:** `password`
