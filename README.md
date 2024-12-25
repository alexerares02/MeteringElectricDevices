# Microservice Application

This repository contains a microservice application consisting of two microservices: **User** and **Device**, along with a frontend interface. The application is configured to run using Docker and orchestrated with Docker Compose.

## Prerequisites

Before you begin, ensure you have the following installed on your machine:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Getting Started

Follow these steps to run the application:

1. **Clone the Repository**

    Open a terminal and clone the repository:

    git clone https://gitlab.com/alexerares02/ds2024_30645_alexe_raresnicusor_assignment_1

    cd your-repository-name

2. **Configure Environment Variables**

    Ensure that you have the required environment variables configured. You may create a .env file in the root directory with the   following variables:

    MYSQL_USER=your_mysql_user

    MYSQL_PASSWORD=your_mysql_password

    MYSQL_DATABASE=your_database_name

3. **Build and Run the Application**

    Use Docker Compose to build and start the application:

    docker-compose up --build
        
    This command will build the services and start them in detached mode.

4. **Access the Application**

    Once the services are running, you can access the frontend using the following URL:

    Frontend: http://localhost:5173

    You can also access the User and Device services at their respective URLs (default ports may vary).

## Stopping the Application

To stop the application, run the following command in the terminal:

docker-compose down

## Troubleshooting

If you encounter any issues while running the application, consider the following:

Ensure that Docker and Docker Compose are installed correctly.

Check the logs for each service to identify any specific errors:

docker-compose logs


