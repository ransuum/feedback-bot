# Employee Feedback Bot & Analysis Platform

This project is a sophisticated system designed to collect, analyze, and manage anonymous employee feedback via a Telegram bot. It was developed as a solution for the Java Junior Developer test task.

The system consists of two main components:
1.  A **Telegram Bot** that serves as the primary interface for employees to register and submit feedback.
2.  A **Spring Boot Web Application** that provides a powerful admin panel to view, filter, and analyze all submitted feedback.

The application leverages the OpenAI API for intelligent text analysis, automatically determining feedback sentiment, criticality, and a suggested solution. It also integrates with Google Docs and Trello for seamless reporting and task management.

## ✨ Core Features

-   **🤖 Telegram Bot Interaction:**
    -   **Seamless Registration:** On first use, the bot guides the user through a simple registration process to select their position (e.g., Mechanic, Electrician) and branch.
    -   **Effortless Feedback Submission:** Registered users can send any text message to the bot, which is treated as a piece of feedback.

-   **🧠 AI-Powered Analysis (via OpenAI):**
    -   **Sentiment Analysis:** Automatically classifies feedback as `POSITIVE`, `NEUTRAL`, or `NEGATIVE`.
    -   **Criticality Assessment:** Assigns a criticality score from 1 (minor) to 5 (urgent).
    -   **Solution Suggestion:** Generates a recommended course of action to address the feedback.

-   **💾 Database Persistence:**
    -   All user information and feedback records are securely stored in a **PostgreSQL** database.

-   **🔄 Automated Integrations:**
    -   **Google Docs Logging:** Every piece of feedback is automatically appended to a central Google Document for easy, non-technical access and historical record-keeping.
    -   **Trello Task Creation:** For feedback marked as critical (level 4 or 5), a new card is automatically created on a specified Trello board for immediate action by management.

-   **💻 Web Admin Panel:**
    -   A comprehensive web interface built with Spring Boot & Thymeleaf, accessible at `http://localhost:8080/feedbacks`.
    -   **Advanced Filtering:** Allows administrators to search and filter feedback by branch, position, sentiment, criticality level, and date range.
    -   **Paginated Results:** Efficiently handles large volumes of feedback.
    -   **Detailed View:** A modal window provides a full, detailed view of each feedback item, including the original message and all analysis results.

## 🏛️ System Architecture

The application follows a modern, service-oriented architecture designed for scalability and maintainability.

 <!-- It's highly recommended to create and link a simple architecture diagram -->

### Tech Stack
-   **Backend:** Java 21, Spring Boot 3.5.5
-   **Database:** PostgreSQL
-   **Data Access:** Spring Data JPA / Hibernate
-   **Frontend (Admin Panel):** Thymeleaf, Bootstrap 5
-   **Bot Framework:** TelegramBots API
-   **AI Analysis:** OpenAI API
-   **Integrations:** Google Docs API, Trello API
-   **Utilities:** Lombok, Jackson

## 🚀 Getting Started

Follow these steps to set up and run the project locally.

### 1. Prerequisites

-   **Java JDK 21** or later.
-   **Apache Maven**.
-   **PostgreSQL** database server.
-   An IDE such as **IntelliJ IDEA**.

### 2. Obtain API Keys & Credentials

You will need to obtain credentials for all external services:

-   **Telegram Bot:**
    1.  Talk to `@BotFather` on Telegram.
    2.  Create a new bot to get your **Bot Token** and **Username**.
-   **OpenAI:**
    1.  Sign up on the [OpenAI Platform](https://platform.openai.com/).
    2.  Go to the API keys section and create a new **API Key**.
-   **Trello:**
    1.  Get your **API Key** at [trello.com/app-key](https://trello.com/app-key).
    2.  Manually generate a **Token** from the same page.
    3.  Create a board and a list for critical feedback. Find the **List ID** by opening a card on that list and adding `.json` to the URL.
-   **Google Docs:**
    1.  Create a project in the [Google Cloud Console](https://console.cloud.google.com/).
    2.  Enable the **Google Docs API**.
    3.  Create a Service Account and download its **credentials JSON file** (e.g., `credentials.json`).
    4.  Create a new Google Doc and get its **Document ID** from the URL.
    5.  Share the Google Doc with the service account's email address, giving it "Editor" permissions.

### 3. Configure the Application

1.  Place your downloaded Google Service Account `credentials.json` file inside the `src/main/resources` directory.
2.  In the `src/main/resources` directory, create a file named `application.properties`.
3.  Fill in the file with your credentials obtained in the previous step, using the template below.

```properties
# ========================================
#  APPLICATION CONFIGURATION
# ========================================

# === DATABASE CONFIGURATION ===
spring.datasource.url=jdbc:postgresql://localhost:5432/feedback_db
spring.datasource.username=postgres
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# === TELEGRAM BOT CONFIGURATION ===
telegram.bot.token=YOUR_TELEGRAM_BOT_TOKEN
telegram.bot.username=YOUR_TELEGRAM_BOT_USERNAME

# === OPENAI CONFIGURATION ===
openai.api.key=YOUR_OPENAI_API_KEY

# === TRELLO INTEGRATION ===
trello.api.key=YOUR_TRELLO_API_KEY
trello.api.token=YOUR_TRELLO_TOKEN
trello.api.list-id=YOUR_TRELLO_LIST_ID

# === GOOGLE DOCS INTEGRATION ===
google.docs.document-id=YOUR_GOOGLE_DOC_ID
google.docs.credentials-path=credentials.json
```

### 4. Set up the Database

Create a new database in PostgreSQL. For example, using the `psql` command-line tool:
```sql
CREATE DATABASE feedback;
```
Spring Boot with Hibernate (`ddl-auto: update`) will automatically create the necessary tables on the first run.

### 5. Run the Application

You can run the application in several ways:

**A) From your IDE (Recommended):**
1.  Open the project in IntelliJ IDEA.
2.  Wait for Maven to download all dependencies.
3.  Find the main application class (the one with the `@SpringBootApplication` annotation).
4.  Right-click and select "Run".

**B) Using Maven from the command line:**
Navigate to the project's root directory and run:
```bash
mvn spring-boot:run
```

The application will start, and the Telegram bot will begin polling for updates.

## 🛠️ Usage

### Telegram Bot

1.  Find your bot on Telegram using its username.
2.  Send the `/start` command.
3.  The bot will prompt you to select your position from a custom keyboard.
4.  Next, it will ask for your branch name.
5.  Once registered, any text message you send will be processed as feedback. You will receive a confirmation after your feedback is analyzed.

### Web Admin Panel

1.  Open your web browser and navigate to `http://localhost:8080/feedbacks`.
2.  You will see a paginated list of all feedback received.
3.  Use the filter form at the top to narrow down results by branch, position, sentiment, criticality, or date.
4.  Click the "Деталі" button on any row to open a modal with the full, detailed information about that feedback, including the original message and AI-generated solution.

---
