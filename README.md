# 🛍️ SmartStore ERP

SmartStore ERP is a full-featured enterprise backend system designed for inventory, production, and store management. It includes authentication, product tracking, production workflows, ordering modules, alerts, and data export support — all built using Spring Boot following a clean layered architecture.

This system is scalable and suitable for retail stores, distributors, warehouses, and small manufacturing businesses.



## 📖 Overview

SmartStore ERP provides a structured backend using Java Spring Boot that handles complete store and warehouse operations including stock monitoring, user management, and production tracking.  

It uses a REST API architecture with role-based login, and JPA for database persistence.


## 🚀 Features

✔ Role-Based Login (Admin / Staff / Customer)  
✔ Passwords Hashed with BCrypt  
✔ Role-Based Access (Admin / User)  
✔ Product Inventory Management  
✔ Order Creation and Status Tracking  
✔ Production Batch Logging  
✔ Low-Stock & System Alerts  
✔ Export & Reporting Module  
✔ Full CRUD Functionality  
✔ Clean Layered Architecture  
✔ Database Auto-Schema using Hibernate  



## 🛠 Technology Stack

| Category | Technology |
|---------|------------|
| Language | Java (17+) |
| Framework | Spring Boot |
| Authentication | Role-based login (admin, staff, customer) |
| ORM | Hibernate + Spring Data JPA |
| Database | MySQL |
| Build Tool | Maven |
| API Style | REST |
| Optional Tools | Node.js (future UI) |


## 🧱 System Architecture

```
                ┌───────────────────────────────┐
                │         Frontend UI           │
                │ (Web App / Mobile / Postman) │
                └───────────────▲───────────────┘
                                │
                          (REST API Calls)
                                │
        ┌───────────────────────┴────────────────────────┐
        │                Controllers Layer                │
        │ Handles HTTP requests and maps them to service │
        └───────────────────────▲────────────────────────┘
                                │
                                │ (Business Logic Calls)
                                │
        ┌───────────────────────┴────────────────────────┐
        │                 Service Layer                  │
        │ Contains business rules and validation         │
        └───────────────────────▲────────────────────────┘
                                │
                                │ (Repository Access)
                                │
        ┌───────────────────────┴────────────────────────┐
        │              Repository Layer                  │
        │ Database queries via Spring Data JPA           │
        └───────────────────────▲────────────────────────┘
                                │
                                │ (SQL Persistence)
                                │
                ┌───────────────┴────────────────┐
                │         MySQL Database          │
                └──────────────────────────────────┘
```



## 📁 Folder Structure

```
SmartStore-ERP/
│
├── pom.xml                                 # Maven project configuration
├── package.json                            # Placeholder for optional UI tooling
│
├── src/
│   ├── main/
│   │   ├── java/com/smartstore/
│   │   │   ├── controller/                # REST API controllers
│   │   │   ├── model/                     # Entities and database models
│   │   │   ├── repository/                # JPA repositories
│   │   │   ├── service/                   # Business logic services
│   │   │   └── config/                    # configuration
│   │   │
│   │   └── resources/
│   │       ├── application.properties      # DB + environment config
│   │       └── static/templates            # Future UI (optional)
│   │
│   └── test/java/                         # Unit + integration tests
│
└── target/                                # Build output and executable JAR
```



## ⚙️ Installation

### 1️⃣ Clone the Repository

```bash
git clone <your-repo-url>
cd SmartStore-ERP
```

### 2️⃣ Install Dependencies

```bash
mvn clean install
```



## 🔧 Configuration

The database connection is read from environment variables, so no password is stored in the
repository. Set them before starting the app:

```bash
# Windows (PowerShell)
$env:DB_PASSWORD = "your-mysql-password"
# macOS / Linux
export DB_PASSWORD="your-mysql-password"
```

| Variable | Default |
|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/store_db` |
| `DB_USERNAME` | `root` |
| `DB_PASSWORD` | none (required) |

The defaults live in `store3/src/main/resources/application.properties`.



## ▶ Running the Application

### Using Maven:

```bash
mvn spring-boot:run
```

### OR Build & Run JAR:

```bash
mvn package
java -jar target/SmartStore-ERP.jar
```

App runs at:

👉 `http://localhost:8080/`



## 🔗 API Endpoints

| Category | Method | Endpoint |
|---------|--------|----------|
| Auth | POST | `/api/auth/login` |
| Users | GET | `/api/users` |
| Products | GET/POST/PUT/DELETE | `/api/products` |
| Orders | GET/POST | `/api/orders` |
| Alerts | GET | `/api/alerts` |
| Production | POST | `/api/production` |
| Export | GET | `/api/export` |



## 🗃 Database Schema (Conceptual)

Tables include:

- `users`
- `products`
- `orders`
- `suppliers`
- `alerts`
- `production_batches`
- `exports_logs`

Hibernate manages schema automatically.



## 🧪 Testing

Run all tests:

```bash
mvn test
```



## 🚀 Future Enhancements

- Web Dashboard UI (React/Angular)
- Mobile Application Integration
- AI-based Inventory Forecasting
- Barcode Scanner Support
- Cloud Deployment (AWS/Docker/Kubernetes)


## 📜 License

This project is licensed under the **MIT License**.


