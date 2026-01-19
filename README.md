# Notification Service

A robust microservice designed to handle multi-channel notifications, currently supporting Email (SMTP) and providing a scalable architecture for SMS, WhatsApp, and Push notifications.

## 🚀 Features

-   **Multi-Channel Support**: Unified API to trigger notifications across different channels.
    -   **Email**: Fully functional SMTP integration for sending emails.
    -   **SMS / WhatsApp / Push**: Architecture in place (currently logs requests, ready for provider integration).
-   **Asynchronous Processing**: Non-blocking notification dispatch for high performance.
-   **Audit Logging**: Comprehensive logging of all notification attempts and statuses in the database.
-   **Scalable Architecture**: Built with Spring Boot and modular provider patterns.

## 🛠️ Tech Stack

-   **Language**: Java 17+
-   **Framework**: Spring Boot 3.x
-   **Build Tool**: Gradle (Kotlin DSL)
-   **Database**: MySQL
-   **Testing**: JUnit 5

## 📋 Prerequisites

Before you begin, ensure you have the following installed:
-   JDK 17 or higher
-   MySQL Database
-   Git

## ⚙️ Configuration

1.  **Clone the repository**
    ```bash
    git clone https://github.com/suresh213/scm-notification-service.git
    cd scm-notification-service
    ```

2.  **Set up Environment Variables**
    Copy the example environment file to create your local interface configuration.
    ```bash
    cp .env.example .env
    ```

3.  **Update `.env`**
    Edit the `.env` file with your database and mail server credentials.

    | Variable | Description | Example |
    | :--- | :--- | :--- |
    | `DB_URL` | MySQL Connection URL | `jdbc:mysql://localhost:3306/notification_db` |
    | `DB_USERNAME` | Database Username | `root` |
    | `DB_PASSWORD` | Database Password | `password` |
    | `MAIL_HOST` | SMTP Host | `smtp.gmail.com` |
    | `MAIL_PORT` | SMTP Port | `587` |
    | `MAIL_USERNAME` | SMTP Username | `user@example.com` |
    | `MAIL_PASSWORD` | SMTP Password | `app-password` |
    | `MAIL_FROM` | Sender Email Address | `noreply@example.com` |

## 🏃‍♂️ Running the Application

Use the Gradle wrapper to run the application locally.

```bash
# MacOS / Linux
./gradlew :app:bootRun

# Windows
.\gradlew.bat :app:bootRun
```

The application will start on port `8080` (default).

## 🔌 API Reference

### Health Check

**Endpoint**
`GET /api/notification/health`

**Response**
```text
Notification Service is healthy
```

### Trigger Notification

**Endpoint**
`POST /api/notification/trigger`

**Headers**
- `Content-Type: application/json`

**Request Body**

| Field | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `channel` | String | Yes | `EMAIL`, `SMS`, `WHATSAPP`, or `PUSH` |
| `recipient` | String | Yes | Email address or phone number/token |
| `subject` | String | No | Subject line (required for Email) |
| `content` | String | Yes | Message body |

**Example (Email)**
```json
{
  "channel": "EMAIL",
  "recipient": "user@example.com",
  "subject": "Welcome to Our Service",
  "content": "Hello, thank you for signing up!"
}
```

**Example (SMS)**
```json
{
  "channel": "SMS",
  "recipient": "+1234567890",
  "content": "Your OTP is 1234"
}
```

**Response**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "status": "QUEUED",
  "timestamp": "2023-10-27T10:00:00Z"
}
```

## 🧪 Running Tests

To execute unit and integration tests:

```bash
./gradlew test
```

## 🏗️ Project Structure

```
├── app
│   ├── src
│   │   ├── main
│   │   │   ├── java/scm/notification
│   │   │   │   ├── config          # App configurations (Async, etc.)
│   │   │   │   ├── controller      # REST Controllers (API Endpoints)
│   │   │   │   ├── dto             # Data Transfer Objects (Request/Response)
│   │   │   │   ├── entity          # JPA Entities (DB Models)
│   │   │   │   ├── enums           # Enumerations (Channel, Status)
│   │   │   │   ├── event           # Event Handling (Listeners)
│   │   │   │   ├── exception       # Global Exception Handling
│   │   │   │   ├── provider        # Notification Providers (Email, SMS, etc.)
│   │   │   │   ├── repository      # Data Access Layer (JPA Repositories)
│   │   │   │   ├── service         # Business Logic Layer
│   │   │   │   ├── validation      # Custom Validators
│   │   │   │   └── NotificationApplication.java
│   │   │   └── resources
│   │   │       ├── templates       # Email Templates (Thymeleaf/HTML)
│   │   │       └── application.yml # Main Configuration
│   └── build.gradle.kts            # Module-level Build Config
├── gradle                          # Gradle Wrapper & Versions
├── build.gradle.kts                # Root Build Config
├── settings.gradle.kts             # Project Settings
├── versions.toml                   # Dependency Version Catalog
└── .env                            # Environment Variables (GitIgnored)
```
