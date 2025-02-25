# CustomerAccount Bank Microservices

This is a project in favor of a task required for Warba Bank, it is about two Microservices Account and Customer that are co-dependant

![Logo](https://i.ibb.co/qL8PGgPd/diagram-export-2-25-2025-5-28-07-PM.png)





## Authors

- [@RashedMaaitah](https://www.github.com/RashedMaaitah)


## Technologies

- Spring boot 3.4.3 with Maven and Java 21
- Keycloak for authentication and authorization
- RabbitMQ for event-driven communication between the 2 services
- API Gateway for centralized access to the services
- JaCoCo for test coverage
- Docker


## Setup

Clone the project

```bash
  git clone https://github.com/RashedMaaitah/Customer-Account-Bank-Micro-Services.git
```

Start the docker containers using the docker compose file
```bash
  docker compose up -d
```

RabbitMQ
- Navigate to using localhost:15672
- Sign in using "guest" for both username and password
- Create a queue called customer.deleted.queue

Keycloak
- Create a Real and name it bank-realm
- Create a client named auth-client

## Run Locally

Using maven run the services as follows:
- Eureka discovery
- Customer service
- Account service
- Gateway


## Obstacles Faced
#### 1. Keycloak Integration:
Keycloak was a new tool for me, and integrating it into the project proved to be challenging. The complexity of managing authentication and authorization through Keycloak, while maintaining proper security protocols, required significant learning and troubleshooting.

#### 2. Choosing Between RabbitMQ and Kafka:
Initially, I struggled to choose between RabbitMQ and Kafka for event-driven communication. After evaluating the two, I opted for RabbitMQ due to its simplicity and ease of integration with Spring Boot, as well as its suitability for lightweight message queues. Kafka would have been more complex for this use case and was overkill for the project's scope.

#### 3. Zero Trust Policy:
Implementing the Zero Trust security policy in conjunction with Keycloak for user authentication added another layer of complexity. Ensuring that all communications were authenticated and authorized correctly in a decentralized environment required thorough attention to detail.

#### 4. Lack of Time for Enhancements:
Due to time constraints, there were several planned enhancements I could not complete, such as:

- Writing additional tests to ensure full coverage.
- Adding more comprehensive documentation.
- Integrating OpenAPI or Swagger for API documentation.
  Despite these challenges, the project succeeded in meeting the core requirements, and the lessons learned will aid in improving future work on similar systems.
