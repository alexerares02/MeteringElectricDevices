

# Microservice Application

This repository contains a microservice application consisting of the following components:

- **User Microservice**: Manages user-related data.
- **Device Microservice**: Handles device information and interactions.
- **Monitoring Microservice**: Communicates with simulators and devices for monitoring activities.
- **Chat Microservice**: Facilitates real-time communication between the admin and users via WebSocket, secured with JWT token authorization.
- **Frontend Interface**: Provides a user-friendly interface for interacting with the system.

The application is configured to run using **Docker**, orchestrated with **Docker Compose**, and uses **RabbitMQ** for inter-service communication. **Traefik** is used as a reverse proxy and load balancer.

---

## Prerequisites

Before you begin, ensure you have the following installed on your machine:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

---

## Getting Started

Follow these steps to run the application:

### 1. Clone the Repository

Open a terminal and clone the repository:

```bash
git clone https://github.com/alexerares02/MeteringElectricDevices
cd your-repository-name
```

### 2. Configure Environment Variables

Create a `.env` file in the root directory with the following variables:

```env
MYSQL_USER=your_mysql_user
MYSQL_PASSWORD=your_mysql_password
MYSQL_DATABASE=your_database_name
RABBITMQ_USER=your_rabbitmq_user
RABBITMQ_PASSWORD=your_rabbitmq_password
```

### 3. Build and Run the Application

Use Docker Compose to build and start the application:

```bash
docker-compose up --build
```

This command will build the services and start them.

### 4. Access the Application

Once the services are running, you can access the components at the following URLs:

- **Frontend**: [http://localhost:5173](http://localhost:5173)
- **Traefik Dashboard**: [http://localhost:8080](http://localhost:8080) (if enabled in `docker-compose.yml`)
- **RabbitMQ Management Interface**: [http://localhost:15672](http://localhost:15672) (default credentials: guest/guest)

The microservices (User, Device, Monitoring, and Chat) will be accessible via Traefik.

---

## Microservices Communication

- **RabbitMQ**: Facilitates messaging between the `monitoring-service`, simulators, and the `device-service`.
- **Traefik**: Routes requests to the appropriate microservices and handles load balancing to ensure scalability.
- **Chat Microservice**: Uses WebSocket for real-time communication between admin and user, secured with JWT token authorization. The WebSocket endpoint is accessible at `ws://localhost:PORT/chat`, and only authenticated users can access it by passing a valid JWT token.

---

## Stopping the Application

To stop the application, run:

```bash
docker-compose down
```

---

## Troubleshooting

- **Verify Prerequisites**: Ensure Docker and Docker Compose are installed and functioning correctly.
- **Check Service Logs**: If you encounter issues, review the logs for individual services to pinpoint errors:

  ```bash
  docker-compose logs
  ```

- **RabbitMQ Connectivity**: Ensure RabbitMQ is running, and the credentials in the `.env` file are correct.
- **Traefik Routing**: Verify that the `docker-compose.yml` file correctly defines the Traefik rules for routing requests.

---
