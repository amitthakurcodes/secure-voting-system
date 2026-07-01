# VoteSecure — Secure Voting System

A secure, role-based online voting platform built with **Spring Boot**, **Spring Security**, and **Thymeleaf**. VoteSecure allows registered users to cast a single verified vote, while administrators manage candidates and monitor results through a dedicated dashboard.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Default Admin Access](#default-admin-access)
- [Security](#security)
- [Screens](#screens)
- [Roadmap](#roadmap)

---

## Overview

VoteSecure is designed to demonstrate a real-world, production-style voting workflow — from user registration and authentication to candidate selection and result tracking — while following secure coding practices such as password hashing and role-based access control.

The UI follows an "official ballot / ledger" design language: a navy-and-violet color palette (inspired by the indelible ink used in Indian elections), serif headlines, and monospace numerals for vote counts and statistics — giving the app a trustworthy, document-like feel rather than a generic dashboard look.

---

## Features

### User Side
- User registration with real-time form validation (name, email, phone, password strength meter)
- Secure login with show/hide password toggle
- **Forgot Password** flow — verifies identity via registered **email + phone number**, then allows setting a new password (no email/SMTP dependency)
- One vote per user, enforced at the application level
- Candidate selection with a **confirmation modal** before the vote is finally submitted
- Personal dashboard showing profile details, voting status, and a uniquely generated avatar (via DiceBear, seeded from the user's email)
- "Already Voted" state clearly displayed after voting

### Admin Side
- Fixed, pre-provisioned admin account (no public admin signup)
- Automatically created at application startup if not already present in the database
- Role-based redirection: Admin and Normal users are routed to separate dashboards after login
- Admin dashboard for managing candidates and monitoring voting activity

### UI / UX
- Fully responsive design (mobile, tablet, desktop)
- Custom typography: **Fraunces** (headings), **Inter** (body), **IBM Plex Mono** (stats/numbers)
- Animated hero section with an SVG "ink stamp" seal
- Animated count-up statistics strip (population, registered voters, turnout, constituencies)
- Card-based, icon-prefixed forms with inline validation feedback
- Success/failure modals and toasts instead of plain redirects

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 19 |
| Framework | Spring Boot |
| Security | Spring Security (role-based access, BCrypt password hashing) |
| Data Access | Spring Data JPA / Hibernate |
| Database | MySQL |
| Template Engine | Thymeleaf |
| Frontend | HTML5, CSS3, Vanilla JavaScript |
| Build Tool | Maven |

---

## Architecture

```
Browser (Thymeleaf-rendered HTML/CSS/JS)
        │
        ▼
Spring MVC Controllers (UserController, AdminController)
        │
        ▼
Service Layer (UserService, CandidateService)
        │
        ▼
Repository Layer (Spring Data JPA — UserRepository, CandidateRepository)
        │
        ▼
MySQL Database
```

**Authentication & Authorization** is handled by Spring Security:
- `CustomUserDetailsService` loads user data from the database and wraps it in `CustomUserDetails`
- `CustomSuccessHandler` inspects the authenticated user's role after login and redirects to `/admin` or `/user` accordingly
- Passwords are hashed using `BCryptPasswordEncoder` — plain-text passwords are never stored or compared

---

## Project Structure

```
src/main/java/com/
├── config/
│   ├── MyConfig.java                 # Spring Security configuration
│   ├── CustomUserDetails.java
│   ├── UserDetailsServiceImpl.java
│   ├── CustomSuccessHandler.java     # Role-based post-login redirect
│   └── AdminInitializer.java         # Auto-creates the admin account on startup
├── controller/
│   ├── UserController.java
│   └── AdminController.java
├── model/
│   ├── User.java
│   └── Candidate.java
├── repository/
│   ├── UserRepository.java
│   └── CandidateRepository.java
└── service/
    ├── UserService.java
    └── CandidateService.java

src/main/resources/
├── templates/
│   ├── base.html                     # Public layout (navbar: Home/Register/Login/About)
│   ├── home.html                     # Public landing page
│   ├── register.html
│   ├── signin.html
│   ├── forgot-password.html
│   ├── about.html
│   ├── admin/
│   │   ├── base.html
│   │   └── dashboard.html
│   └── user/
│       ├── base.html                 # Authenticated layout (navbar: Home/Welcome/Logout)
│       └── dashboard.html
├── static/
│   ├── css/style.css
│   └── image/
└── application.properties
```

---

## Getting Started

### Prerequisites
- Java 19+
- Maven
- MySQL Server (running locally or remotely)

### Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd MySpring_Boot_aa23v_VotingApp_Final
   ```

2. **Configure the database**

   Update `src/main/resources/application.properties` with your MySQL credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/<your_database_name>
   spring.datasource.username=<your_mysql_username>
   spring.datasource.password=<your_mysql_password>
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Open in browser**
   ```
   http://localhost:8080
   ```

On first startup, the application automatically creates a default admin account in the database (see below) if one does not already exist.

---

## Default Admin Access

The admin account is **not created through the public registration form**. It is automatically provisioned at application startup via `AdminInitializer` (a `CommandLineRunner`), with its password securely hashed using BCrypt.

| Field | Value |
|---|---|
| Email | `admin` |
| Password | `admin` |

> ⚠️ For any real deployment, change these default credentials immediately after first login and remove/restrict the initializer logic.

---

## Security

- **Password hashing:** All user and admin passwords are hashed using `BCryptPasswordEncoder` before being persisted — no plain-text passwords are stored.
- **Role-based access control:** Spring Security restricts `/admin/**` to `ROLE_ADMIN` and `/user/**`, `/candidate/**` to `ROLE_NORMAL`.
- **Session-based flash messages:** Success/failure messages (registration status, login errors, logout confirmation) are passed via the HTTP session and cleared immediately after being displayed, preventing stale message leaks.
- **Forgot Password verification:** Identity is confirmed using a combination of registered email and phone number before allowing a password reset — avoiding the need for an external mail service while still requiring two pieces of user-known information.
- **CSRF protection:** Enabled by default via Spring Security.

---

## Screens

- **Home** — Hero section, live-animated statistics strip, feature highlights, "How It Works" steps
- **Register** — Card-based form with icon-prefixed inputs, live validation, and password strength meter
- **Login** — Card-based form with password visibility toggle and a link to Forgot Password
- **Forgot Password** — Two-step flow: verify identity (email + phone) → set new password
- **User Dashboard** — Profile card with auto-generated avatar and voting status, plus a candidate selection panel with a vote confirmation modal
- **Admin Dashboard** — Candidate and results management

---

## Roadmap

Planned/possible future enhancements:

- [ ] Candidate photo upload and management UI (add/edit/delete) for admins
- [ ] Live results visualization (bar/pie charts)
- [ ] CSV/PDF export of results
- [ ] Election start/end time scheduling with a home page countdown
- [ ] OTP-based phone verification during registration
- [ ] CAPTCHA on registration/login to prevent automated abuse
- [ ] Dark mode toggle

---

## License

This project was built for educational purposes as part of a college project submission.
