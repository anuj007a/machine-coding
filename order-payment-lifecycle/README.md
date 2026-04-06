# Order Payment Lifecycle Service

A highly-scalable, production-ready backend service designed to handle the complete lifecycle of order payments safely, efficiently, and idempotently.

---

## 🚀 Features
- **Create Order**: Initialize payments with monetary amounts and specific currencies.
- **Authorize Payment (Idempotent)**: Pre-authorize payments. Safe to retry.
- **Capture Payment (Idempotent)**: Safely capture an authorization. Built-in concurrency control prevents double-capture.
- **Refund (Partial & Full)**: Issue refunds up to the legally captured amount.
- **Fetch Order & History**: Retrieve aggregate order details and a timeline of all payment actions.
- **Daily Reconciliation Report**: Timezone-aware endpoint to generate aggregate business transaction reports.

## 🛠 Tech Stack
- **Framework**: Java 21, Spring Boot 3.5.x, Spring Data JPA
- **Database**: 
  - **H2**: Embedded, used automatically during local `default` profile and testing.
  - **PostgreSQL**: Production-grade database mapped via `prod` profile.
- **Migration**: Flyway (with automatic baseline support).
- **Tooling**: Gradle, Lombok, MapStruct, JUnit 5, Mockito.
- **Documentation**: SpringDoc OpenAPI (Swagger 3).
- **Deployment**: Docker, Docker Compose, Kubernetes (Helm).

---

## 🏗 Design Decisions

### 1. Architecture
- **Layered Architecture**: Controller -> Service -> Repository. This ensures business logic (`PaymentServiceImpl`) is strictly separated from transport logic (`PaymentController`) and database specifics.
- **Pessimistic Locking**: The `OrderRepository` uses `@Lock(LockModeType.PESSIMISTIC_WRITE)` when retrieving orders for payment transitions. This completely prevents race conditions and double-spending/double-capturing in highly concurrent environments.

### 2. Idempotency 
- **Idempotency-Key Header**: Every state-changing API (`authorize`, `capture`, `refund`) strictly requires an `Idempotency-Key` header.
- **IdempotencyService**: The application caches the exact JSON response. If the same key is submitted again, the system halts processing and immediately returns the cached response. 

### 3. Financial Precision
- **Cents/Paise**: All monetary amounts are represented as `Long` (integers) in the smallest denominator. Floating-point data types (`double`, `float`) are strictly avoided to prevent precision loss.

### 4. Exception Handling
- **Centralized `@ControllerAdvice`**: Custom exceptions (`BusinessException`, `NotFoundException`) are automatically mapped to standard REST responses (`400 Bad Request`, `404 Not Found`).

---

## 💻 Local Setup & Execution

### 1. Bare Metal (H2 In-Memory)
By default, the application runs on an in-memory H2 database. Perfect for quick tests and development.

```bash
# 1. Clone the repository
git clone <repository-url>
cd order-payment-lifecycle

# 2. Build the project
./gradlew clean build

# 3. Run the application
./gradlew bootRun
```

### 2. Docker Compose (PostgreSQL Production Emulation)
Run the application with the full PostgreSQL database and PgAdmin GUI using Docker Compose.

```bash
# 1. Start the stack in detached mode
docker-compose up -d --build

# 2. View application logs
docker-compose logs -f app
```
*To tear down everything and delete the persistent database volume:*
```bash
docker-compose down -v
```

---

## 📖 API Documentation & Swagger

Once the application is running (either locally or via Docker), you can access the interactive Swagger UI.

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🧪 End-to-End Testing (cURL)

Below is the complete lifecycle of a single order using `curl`.

#### 1. Create an Order
```bash
curl -s -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"amount": 1000, "currency": "INR"}'
```
*(Copy the `id` from the response for the next steps)*

#### 2. Authorize Payment (Requires Idempotency-Key)
```bash
curl -s -X POST http://localhost:8080/orders/<ORDER_ID>/authorize \
  -H "Content-Type: application/json" \
  -H "Idempotency-Key: auth-123" \
  -d '{"amount": 1000}'
```

#### 3. Capture Payment
```bash
curl -s -X POST http://localhost:8080/orders/<ORDER_ID>/capture \
  -H "Content-Type: application/json" \
  -H "Idempotency-Key: capture-123" \
  -d '{"amount": 1000}'
```

#### 4. Refund Payment (Partial)
```bash
curl -s -X POST http://localhost:8080/orders/<ORDER_ID>/refund \
  -H "Content-Type: application/json" \
  -H "Idempotency-Key: refund-123" \
  -d '{"amount": 500}'
```

#### 5. Fetch Full Order Details & History
```bash
curl -s -X GET http://localhost:8080/orders/<ORDER_ID>
```

#### 6. Generate Reconciliation Report
```bash
curl -s -X GET "http://localhost:8080/orders/reconciliation?timezone=Asia/Kolkata"
```

---

## 🚢 Kubernetes Deployment (Helm)

The repository includes a ready-to-use Helm chart inside the `helm/` directory.

### Installation
```bash
# Move into the helm directory
cd helm

# Install the application into your Kubernetes cluster
helm install my-payment-service ./order-payment-lifecycle
```

### Upgrading 
```bash
helm upgrade my-payment-service ./order-payment-lifecycle
```

### Uninstallation
```bash
helm uninstall my-payment-service
```

The Helm chart includes:
- **Deployment**: Highly-available, configurable pod replication (default 2 replicas).
- **Service**: Internal routing.
- **HPA**: Horizontal Pod Autoscaling based on CPU/Memory usage.
- **Probes**: Configured Liveness and Readiness checks pointing to Spring Actuator (`/actuator/health`).

---

## 👨‍💻 Author
Payment Services Team