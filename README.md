[![LinkedIn][linkedin-shield]][linkedin-url]

[![CI][ci-shield]][ci-url]

<br />
<div align="center">
<h3 align="center">Notification Service</h3>

  <p align="center">
    Event-driven microservice responsible for sending notifications within the Order Fulfillment Platform.
    <br />
    <br />
    <a href="https://github.com/order-fulfillment-platform">View Organization</a>
  </p>
</div>

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#about">About</a></li>
    <li><a href="#built-with">Built With</a></li>
    <li><a href="#events">Events</a></li>
    <li><a href="#getting-started">Getting Started</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

## About

The Notification Service is the final consumer in the Order Fulfillment Platform event chain. It listens to the outcome events from the payment and inventory services and sends notifications to customers accordingly.

In this MVP implementation, notifications are simulated via application logs. In a production environment, this service would integrate with an email provider such as SendGrid or AWS SES.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Built With

[![Spring Boot][springboot-shield]][springboot-url]
[![Apache Kafka][kafka-shield]][kafka-url]
[![Docker][docker-shield]][docker-url]
[![Java][java-shield]][java-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Events

### Consumed

| Topic | Event | Notification |
|---|---|---|
| payment.authorized | PAYMENT_AUTHORIZED | Order confirmation email |
| payment.failed | PAYMENT_FAILED | Payment failure email |
| stock.rejected | STOCK_REJECTED | Out of stock email |

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- Docker

### Run with Docker Compose

Start the full platform from the [infrastructure](https://github.com/order-fulfillment-platform/infrastructure) repository:
```bash
docker-compose up -d --build
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Contact

Eros Burelli — [LinkedIn](https://www.linkedin.com/in/eros-burelli-a458b1145/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS -->
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/eros-burelli-a458b1145/
[springboot-shield]: https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white
[springboot-url]: https://spring.io/projects/spring-boot
[kafka-shield]: https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white
[kafka-url]: https://kafka.apache.org/
[docker-shield]: https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white
[docker-url]: https://www.docker.com/
[java-shield]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[java-url]: https://www.java.com/
[ci-shield]: https://github.com/order-fulfillment-platform/notification-service/actions/workflows/ci.yml/badge.svg
[ci-url]: https://github.com/order-fulfillment-platform/notification-service/actions/workflows/ci.yml