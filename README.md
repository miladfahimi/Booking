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

## ⚡ Running with Tilt (Alternative Dev Workflow)

[Tilt](https://tilt.dev) provides a **smart development loop** for multi-service applications.  
It automatically rebuilds and redeploys containers when your source code changes — ideal for rapid iteration across multiple backend and frontend microservices.

### 1️⃣ Install Tilt

For Linux / WSL:

```sh
curl -fsSL https://raw.githubusercontent.com/tilt-dev/tilt/master/scripts/install.sh | bash
```

Verify the installation:

```sh
tilt version
```

### 2️⃣ Start the Development Environment

From the project root, run:

```sh
tilt up
```

Tilt will:

- Read the `Tiltfile` in the repository root.
- Use `devops/docker-compose.dev.yml` to start all defined services.
- Watch for code changes in backend and frontend directories.
- Automatically rebuild and synchronize containers without full restarts.
- Provide a local web dashboard at [http://localhost:10350](http://localhost:10350).

### 3️⃣ Access Running Services

Once Tilt is active:

| Service | Port | URL |
|----------|------|-----|
| **Frontend** | 4200 | [http://localhost:4200](http://localhost:4200) |
| **Authentication** | 8082 | [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html) |
| **BFF** | 8083 | [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html) |
| **Profile** | 8084 | [http://localhost:8084/swagger-ui.html](http://localhost:8084/swagger-ui.html) |
| **Reservation** | 8085 | [http://localhost:8085/swagger-ui.html](http://localhost:8085/swagger-ui.html) |
| **Provider** | 8086 | [http://localhost:8086/swagger-ui.html](http://localhost:8086/swagger-ui.html) |
| **SMTP UI** | 8025 | [http://localhost:8025](http://localhost:8025) |

### 4️⃣ Stopping Tilt

To gracefully stop Tilt and all containers:

```sh
tilt down
```

Or simply press `Ctrl + C` in the terminal running Tilt.

### 5️⃣ Benefits of Using Tilt

- 🌀 **Auto rebuild & redeploy** — any code change triggers a container refresh.  
- 🔥 **Live code sync** — Java and Angular code updates are reflected immediately.  
- 🧠 **Unified dashboard** — view logs, status, and live updates for all services in one place.

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
