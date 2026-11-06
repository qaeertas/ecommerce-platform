# E-Commerce Microservices Platform

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-brightgreen?style=flat-square&logo=springboot)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2025.1-success?style=flat-square&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker)

A distributed E-Commerce backend platform built with **Java 21** and the **Spring Boot / Spring Cloud** ecosystem.

## Architecture

| Service | Port | Description | Database |
| :--- | :--- | :--- | :--- |
| **`api-gateway`** | `8080` | Single entry point (Spring Cloud Gateway WebFlux). Routes requests to internal services. | — |
| **`product-service`** | `8081` | Product catalog, categories, and atomic stock reduction. | `product_db` (`5432`) |
| **`cart-service`** | `8082` | Shopping cart management and real-time stock validation via `product-service`. | `cart_db` (`5433`) |
| **`order-service`** | `8083` | Order placement, historical data snapshots, stock deduction, and cart clearing. | `order_db` (`5434`) |

## Tech Stack

* **Java 21**
* **Spring Boot 4.1** (WebMVC, Data JPA / Hibernate 7, Validation)
* **Spring Cloud Gateway** (Reactive WebFlux)
* **Spring Declarative HTTP Client** (`@HttpExchange` + `RestClient`)
* **PostgreSQL 16** (Database per Service pattern)
* **MapStruct & Lombok**
* **Maven** (Multi-module Monorepo)
* **Docker & Docker Compose**

## Getting Started

### Prerequisites
* Java 21 & Maven
* Docker & Docker Compose

### Run with Docker Compose

1. Build executable JARs:
   ```bash
   mvn clean package -DskipTests
   ```

2. Start all containers:
   ```bash
   docker compose up -d --build
   ```

## API Endpoints (via API Gateway `:8080`)

### Products & Categories
* `POST /api/categories` — Create a category
* `GET /api/categories` — Get all categories
* `POST /api/products` — Create a product
* `GET /api/products?categoryId={id}` — Get all products (optional category filter)
* `GET /api/products/{id}` — Get product by ID
* `PUT /api/products/{id}` — Update product
* `PUT /api/products/{id}/reduce-stock?quantity={qty}` — Reduce product stock
* `DELETE /api/products/{id}` — Delete product

### Shopping Cart
* `GET /api/carts/{userId}` — Get user cart
* `POST /api/carts/{userId}/items` — Add item to cart
* `DELETE /api/carts/{userId}/items/{productId}` — Remove item from cart
* `DELETE /api/carts/{userId}` — Clear cart

### Orders
* `POST /api/orders/{userId}` — Place an order from user's cart
* `GET /api/orders/{orderId}` — Get order by ID
* `GET /api/orders/user/{userId}` — Get user order history