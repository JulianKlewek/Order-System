# Order System

This project is a web application created in Spring Boot which allows users to place simple orders for chosen products. Project is build in microservice architecture, all modules are containerized.


## Tech

Order System is developed using following technologies: <br>
![image](https://img.shields.io/badge/17-Java-orange?style=for-the-badge) &nbsp;
![image](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring) &nbsp;
![image](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white) &nbsp;

Testing:<br>
![image](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Mockito-78A641?style=for-the-badge) &nbsp;
![image](https://img.shields.io/badge/Testcontainers-9B489A?style=for-the-badge) &nbsp;
![image](https://img.shields.io/badge/WIREMOCK-lightblue?style=for-the-badge) &nbsp;

Other:<br>
![image](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Keycloak-lightblue?style=for-the-badge&logo=keycloak&logoColor=white) &nbsp;

## Specification

- Spring Boot, web application
- Microservice architecture
- Netflix-eureka-server used as discovery service
- Implemented API gateway
- Keycloak as Auth Server
- Resilience4j Circuit Breaker to manage fault tolerance between Order and Inventory Services
- Coverage with unit tests, integration tests
- Full containerization in Docker
- Inventory and Order Services are using separate MySql Databases
- Product Services is using MongoDB
- Kafka for async communication between Order and Notification Services


## Rest-API Endpoints

|       ENDPOINT        | METHOD  |         REQUEST          |       RESPONSE       |                    FUNCTION                     |
|:---------------------:|:-------:|:------------------------:|:--------------------:|:-----------------------------------------------:|
|     /api/product      |  POST   |   JSON BODY (product)    |    JSON (product)    |                creates new product              |
|     /api/product      |  GET    |            -             |    JSON (products)   |                returns all products             |
|     /api/order        |  POST   |   JSON BODY (order)      |    JSON (order)      |                places order for products        |
|/api/inventory?skuCodes|  GET    |   PARAM (skuCodes)       |    JSON (inventory)  |    returns quantity information for skuCodes    |

