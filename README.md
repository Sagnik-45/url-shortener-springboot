# URL Shortener (Spring Boot)

A backend service that converts long URLs into short, unique codes and redirects users efficiently. The system also tracks click analytics and prevents abuse using basic rate limiting.

---

## 🚀 Features

* 🔗 URL shortening using Base62 encoding
* ↪️ HTTP 302 redirection for seamless navigation
* 📊 Click analytics tracking (per URL)
* 🚦 Rate limiting (per IP) to prevent abuse
* 🧱 Clean layered architecture (Controller → Service → Repository)

---

## 🛠 Tech Stack

* Java
* Spring Boot
* Spring Data JPA
* MySQL / H2 Database

---

## ⚙️ Architecture

The application follows a standard layered backend design:

```
Controller → Service → Repository → Database
```

* **Controller** → Handles HTTP requests
* **Service** → Business logic (shortening, redirect, analytics)
* **Repository** → Database interaction using JPA

---

## 📌 API Endpoints

### 🔹 Create Short URL

**POST** `/api/shorten`

Request Body:

```json
{
  "url": "https://example.com"
}
```

Response:

```json
{
  "shortUrl": "http://localhost:8080/api/abc123",
  "code": "abc123"
}
```

---

### 🔹 Redirect to Original URL

**GET** `/api/{code}`

* Redirects to the original URL using HTTP 302

---

### 🔹 Get Click Stats

**GET** `/api/stats/{code}`

Response:

```json
{
  "clicks": 10
}
```

---

## 🧠 Key Concepts Used

* Base62 Encoding for generating short codes
* One-to-Many mapping for click tracking
* REST API design
* Rate limiting using in-memory logic
* HTTP status handling (302, 404, 429)

---

## ▶️ How to Run

1. Clone the repository
2. Open in IDE (IntelliJ / VS Code)
3. Run the Spring Boot application
4. Test APIs using Postman

---

## ⚡ Future Improvements

* Redis caching for faster lookups
* Distributed rate limiting
* Custom alias support
* Expiry time for URLs

---

## 👨‍💻 Author

Sagnik Chatterjee
