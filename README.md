# 🎾 Tennis Time

## 📌 Overview

Tennis Time is a platform designed to streamline **tennis court bookings** for both players and tennis hall owners.\
Our application enables **real-time booking management**, making scheduling easy and efficient.

---

## 🚀 Getting Started

Follow these steps to set up **Tennis Time** on your machine.

---

## 🛠 Installation

### 1️⃣ Clone the Repository

```sh
git clone https://gitlab.com/miladfahimi2012/tennistime.git
cd tennistime
```

### 2️⃣ Set Up Environment Variables

Create a `.env` file in the root directory and **configure your environment variables**:

```ini
# Database Configuration
DB_URL=jdbc:postgresql://postgres:5432/tennistime_test
DB_USERNAME=postgres
DB_PASSWORD=123
DB_NAME=tennistime_test

# Redis Configuration
REDIS_HOST=redis
REDIS_PORT=6379

# JWT Configuration
JWT_SECRET=your_jwt_secret

# Twilio Configuration
TWILIO_ACCOUNT_SID=your_twilio_sid
TWILIO_AUTH_TOKEN=your_twilio_token
TWILIO_SERVICE_SID=your_twilio_service_sid

# SMTP Configuration
SMTP_SERVER_IP=smtp
SMTP_SERVER_PORT=8025
```

📌 **Note:** Replace `your_jwt_secret`, `your_twilio_sid`, `your_twilio_token`, and `your_twilio_service_sid` with actual credentials.

### 3️⃣ Apply Environment Variables

Before running the application, load the environment variables:

```sh
export $(grep -v '^#' .env | xargs)
```

For Windows (PowerShell):

```powershell
Get-Content .env | ForEach-Object { ($name, $value) = $_ -split '='; Set-Item -Path "env:\$name" -Value $value }
```

---

## 🎯 Running in Development Mode

To start Tennis Time in **development mode**, follow these steps:

### 1️⃣ Build the Backend

```sh
mvn clean install
```

### 2️⃣ Run the Application with Docker

```sh
docker-compose -f devops/docker-compose.dev.yml up --build
```

- **This will start:**
  - PostgreSQL (`postgres`)
  - Redis (`redis`)
  - Authentication service (`authentication`)
  - Profile service (`profile`)
  - BFF service (`bff`)
  - Provider service (`provider`)
  - Reservation service (`reservation`)
  - Frontend (`frontend`)

✅ **Once running, access the application at:**\
`http://localhost:8080` (Frontend)\
`http://localhost:8082` (Authentication Service)\
`http://localhost:8083` (BFF Service)\
`http://localhost:8084` (Profile Service)\
`http://localhost:8085` (Reservation Service)\
`http://localhost:8086` (Provider Service)

📌 **To stop the application**, run:

```sh
docker-compose -f devops/docker-compose.dev.yml down
```

---

## 🚀 Running in Production Mode

### 1️⃣ Build the Production Image

```sh
docker-compose -f devops/docker-compose.yml build
```

### 2️⃣ Start the Application in Production

```sh
docker-compose -f devops/docker-compose.yml up -d
```

✅ **The application will be running in detached mode.**

📌 **To stop the production environment**, run:

```sh
docker-compose -f devops/docker-compose.yml down
```

---

## 📂 Project Structure

```
tennistime/
│── authentication/
│── profile/
│── bff/
│── provider/
│── reservation/
│── frontend/
│── devops/
    ├── docker-compose.dev.yml
    ├── docker-compose.prod.yml
    ├── .env

```

---

## 🤝 Contributing

We welcome contributions! To contribute:

1. Fork the repository.
2. Create a new feature branch (`git checkout -b feature-name`).
3. Commit your changes (`git commit -m "Add new feature"`).
4. Push to the branch (`git push origin feature-name`).
5. Open a pull request.

---

## 📞 Support

For support, open an issue in the [GitLab repository](https://gitlab.com/miladfahimi2012/tennistime).

---

## 📜 License

This project is licensed under the **MIT License**.

---

## 📌 Project Status

Tennis Time is actively maintained and developed! 🚀