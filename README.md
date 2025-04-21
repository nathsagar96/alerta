# 🌠 Astro Alert

A distributed microservices application that monitors near-Earth objects (NEOs), identifies potential threats, and
delivers timely notifications about hazardous asteroids.

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Kafka](https://img.shields.io/badge/Kafka-3.5.1-orange.svg)](https://kafka.apache.org/)
[![Java](https://img.shields.io/badge/Java-21-yellow.svg)](https://openjdk.java.net/)

## 📋 Table of Contents
- [📝 Description](#-description)
- [🏗️ System Architecture](#-system-architecture)
- [✨ Features](#-features)
- [🛠️ Technologies Used](#-technologies-used)
- [🚀 Installation \& Setup](#-installation--setup)
    - [Prerequisites](#prerequisites)
    - [Setting Up the Infrastructure](#setting-up-the-infrastructure)
    - [Building the Microservices](#building-the-microservices)
    - [Configuration](#configuration)
- [🖥️ Usage](#-usage)
    - [Running the Services](#running-the-services)
    - [Accessing the System](#accessing-the-system)
- [⚙️ Configuration](#-configuration)
    - [Alerting Service Configuration](#alerting-service-configuration)
    - [Notification Service Configuration](#notification-service-configuration)
- [📚 API Reference](#-api-reference)
    - [NASA NeoWs API](#nasa-neows-api)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)
- [📞 Contact](#-contact)
- [🙏 Acknowledgements](#-acknowledgements)

## 📝 Description

The Astro Alert is a real-time monitoring solution that connects to NASA's Near-Earth Object Web Service (NeoWs) to
track potentially hazardous asteroids that might pose a threat to Earth. By leveraging distributed microservices
architecture with Apache Kafka for messaging, this system provides a robust pipeline for asteroid threat detection and
notification.

## 🏗️ System Architecture

The system consists of two primary microservices:

1. **asteroid-alerting-service**: Periodically fetches NEO data from NASA API, identifies potential threats based on
   configurable criteria, and publishes events to a Kafka topic.

2. **asteroid-notification-service**: Consumes threat events from Kafka, persists them to PostgreSQL, and sends email
   notifications to configured recipients.

## ✨ Features

- **Real-time Monitoring**: Daily checks of NASA's NEO database for potentially hazardous asteroids
- **Configurable Threat Detection**: Customizable parameters for identifying asteroid threats
- **Asynchronous Processing**: Kafka-based messaging for reliable event handling
- **Persistent Storage**: PostgreSQL database for storing all identified threats
- **Email Notifications**: Automated alerts with detailed information about approaching asteroids
- **Scalable Architecture**: Independent microservices that can be scaled separately
- **Comprehensive Logging**: Detailed logging for monitoring and troubleshooting
- **Docker Support**: Easy deployment with Docker Compose

## 🛠️ Technologies Used

- **Java 21**: Core programming language
- **Spring Boot 3.4.4**: Application framework
- **Apache Kafka**: Message broker for asynchronous communication
- **PostgreSQL**: Database for persistent storage
- **Spring Data JPA**: Database access layer
- **Spring Kafka**: Kafka integration for Spring Boot
- **Spring Mail**: Email notification service
- **Maven**: Dependency management and build tool
- **Docker & Docker Compose**: Containerization and orchestration

## 🚀 Installation & Setup

### Prerequisites

- Java Development Kit (JDK) 21
- Maven 3.8+
- Docker and Docker Compose
- NASA API Key (obtain from [api.nasa.gov](https://api.nasa.gov/))

### Setting Up the Infrastructure

1. Clone the repository:
   ```bash
   git clone https://github.com/nathsagar96/astro-alert.git
   cd astro-alert
   ```

2. Start the required infrastructure (PostgreSQL, Kafka) using Docker Compose:
   ```bash
   docker compose up -d
   ```

3. Verify that the services are running:
   ```bash
   docker compose ps
   ```

### Building the Microservices

1. Build the alerting service:
   ```bash
   cd asteroid-alerting-service
   mvn clean package
   ```

2. Build the notification service:
   ```bash
   cd ../asteroid-notification-service
   mvn clean package
   ```

### Configuration

1. Configure your NASA API key in `asteroid-alerting-service/src/main/resources/application.yaml`:
   ```yaml
   nasa:
    api:
      key: YOUR_NASA_API_KEY
   ```

2. Configure email notifications in `asteroid-notification-service/src/main/resources/application.yaml`:
   ```yaml
   spring:
    mail:
      host: your-smtp-server.com
      port: yout-smtp-server-port
      username: your-username
      password: your-password
   
   notification:
    email:
      to: your-email@example.com
   ```

## 🖥️ Usage

### Running the Services

1. Start the alerting service:
   ```bash
   cd asteroid-alerting-service
   java -jar target/asteroid-alerting-service-1.0.0-SNAPSHOT.jar
   ```

2. Start the notification service:
   ```bash
   cd ../asteroid-notification-service
   java -jar target/asteroid-notification-service-1.0.0-SNAPSHOT.jar
   ```

### Accessing the System

- **Kafka UI**: Access the Kafka management interface at [http://localhost:8090](http://localhost:8090)
- **PgAdmin**: Access the PostgreSQL management interface at [http://localhost:5050](http://localhost:5050)
    - Login with email: `admin@example.com` and password: `admin`

## ⚙️ Configuration

### Alerting Service Configuration

Key configuration parameters in `application.yaml`:

```yaml
# Scheduling
scheduler:
  cron:
    expression: 0 0 12 * * ?  # Daily at noon

neo:
  check:
    days_ahead: 7 # Check for next 7 days

# Threat Detection
notification:
  miss_distance:
    threshold:
      km: 5000000  # 5 million km threshold
```

### Notification Service Configuration

Key configuration parameters in `application.yaml`:

```yaml
# Database
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/asteroiddb
  username: postgres
  password: password

# Email
notification:
  email:
    to: alerts@example.com,admin@example.com
```

## 📚 API Reference

### NASA NeoWs API

The system integrates with NASA's Near Earth Object Web Service:

- **Endpoint**: `https://api.nasa.gov/neo/rest/v1/feed`
- **Parameters**:
    - `start_date`: Start date for NEO data (YYYY-MM-DD)
    - `end_date`: End date for NEO data (YYYY-MM-DD)
    - `api_key`: Your NASA API key
- **Documentation**: [NASA NeoWs API Documentation](https://api.nasa.gov/)

## 🤝 Contributing

Contributions to improve the Astro Alert are welcome! Here's how you can contribute:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add some amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

Please make sure to update tests as appropriate and adhere to the existing coding style.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

Sagar Nath - [@nathsagar96](https://github.com/nathsagar96)

## 🙏 Acknowledgements

- [NASA Open APIs](https://api.nasa.gov/) for providing access to NEO data
- [Spring Boot](https://spring.io/projects/spring-boot) for the application framework
- [Apache Kafka](https://kafka.apache.org/) for the messaging platform
