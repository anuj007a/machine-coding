# Wallet Service

Wallet management and transaction management service built with Spring Boot.

## Features

- Wallet creation, balance check, add money, transfer funds
- Transaction history, transaction lookup, paginated transaction listing
- Balance validation and self-transfer prevention
- Ledger entries with balance before/after

## API Commands

- See `curl-commands.md` for end-to-end `curl` examples.

## Local Run

### Prerequisites

- Java 21
- Gradle wrapper (`./gradlew`)
- PostgreSQL (if using default `application.yml`)

### Test

```bash
cd /Users/anuj/repository/machine-coding/wallet-service
./gradlew test
```

### Run (default config)

```bash
cd /Users/anuj/repository/machine-coding/wallet-service
./gradlew bootRun
```

### Run with in-memory H2 (no PostgreSQL)

```bash
cd /Users/anuj/repository/machine-coding/wallet-service
./gradlew bootRun --args='--spring.datasource.url=jdbc:h2:mem:walletdb --spring.datasource.driver-class-name=org.h2.Driver --spring.datasource.username=sa --spring.datasource.password= --spring.jpa.database-platform=org.hibernate.dialect.H2Dialect'
```

## Docker

Build image:

```bash
cd /Users/anuj/repository/machine-coding/wallet-service
docker build -t wallet-service:latest .
```

Run container:

```bash
docker run --rm -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/wallet_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres_Login@25 \
  wallet-service:latest
```

## Docker Compose

```bash
cd /Users/anuj/repository/machine-coding/wallet-service
docker compose up --build
```

## Helm

Chart path: `helm/wallet-service`

Install:

```bash
cd /Users/anuj/repository/machine-coding/wallet-service
helm install wallet-service ./helm/wallet-service
```

Upgrade:

```bash
cd /Users/anuj/repository/machine-coding/wallet-service
helm upgrade wallet-service ./helm/wallet-service
```

Uninstall:

```bash
helm uninstall wallet-service
```
