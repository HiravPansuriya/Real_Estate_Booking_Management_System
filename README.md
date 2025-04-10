# 🏠 Real Estate Property Listing API

A robust and secure REST API built with Spring Boot that facilitates listing, searching, and booking of real estate properties. With role-based access control and clear visibility rules, this system supports **Buyers**, **Sellers**, and **Admins**. The application includes support for secure bookings and appointment management.

---

## 📌 Features

### 👤 Buyer Functionalities:
- Browse and search available properties.
- Book appointments for **AVAILABLE** properties.
- View own bookings with real-time booking status.

### 🧑‍💼 Seller Functionalities:
- Add, update, or delete property listings.
- View and manage bookings on their own properties.
- Update booking status: `PENDING ➡️ CONFIRMED / CANCELLED`.

### 🔐 Admin Functionalities:
- Manage all users, properties, and bookings.
- Oversee application-wide activity.

---

## 🔒 Security & Access Control

This application leverages **Spring Security with HTTP Basic Authentication** and **role-based access** for:

- **Roles**: `BUYER`, `SELLER`, `ADMIN`
- **JdbcUserDetailsManager** for user authentication.
- **BCrypt** for password hashing.
- **CSRF disabled** for REST API compatibility.

**Access Rules:**
- Buyers can book only `AVAILABLE` properties.
- Booking of `SOLD` properties is restricted.
- Duplicate or invalid bookings are prevented.
- Buyes/Sellers **can only access or modify their own data**.
- Bookings are visible only to the property owner and the booking buyer.

---

## 🧰 Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Security (HTTP Basic Authentication)
- MySQL / H2
- Postman (API testing)

---

## 🗂 Project Structure

```bash
real-estate/
├── src/
│   ├── main/
│   │   ├── java/com/Hirav/real-estate/
│   │   │   ├── config/       # Spring Security Configurations
│   │   │   ├── controller/   # Controllers
│   │   │   ├── dao/          # Repositories (Data Access Layer)
│   │   │   ├── entity/       # Entity classes
│   │   │   ├── exception/    # Exception Handling (Error Message in Postman)
│   │   │   └── service/      # Business logic
│   │   └── resources/        # application.properties, static files
│   ├── test/                 # Unit and Integration Tests
├── pom.xml                   # Maven Build File
└── README.md
