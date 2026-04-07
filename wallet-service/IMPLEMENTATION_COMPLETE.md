# Wallet Service - Complete Implementation Document

## 1. Objective

Deliver a production-oriented Wallet and Transaction Management service with:
- wallet lifecycle APIs
- transaction safety constraints
- transfer validation rules
- persistent audit trail
- test coverage
- deployment artifacts (Docker, Compose, Helm)

## 2. Functional Scope Delivered

### Wallet Management
- Create wallet for a user
- Retrieve wallet by wallet ID
- Get wallet balance
- List wallets by user ID
- Update wallet status (`ACTIVE`, `INACTIVE`, `CLOSED`)

### Transaction Management
- Add money (credit)
- Transfer funds between wallets (debit + credit)
- Retrieve transaction by ID
- List transactions by wallet with optional filters:
  - date range (`startDate`, `endDate`)
  - transaction type (`CREDIT`, `DEBIT`)
- Paginated transaction list

### Ledger / Audit
- Record ledger row for each wallet side of movement
- Track:
  - `walletId`
  - `transactionId`
  - `transactionType`
  - `amount`
  - `balanceBefore`
  - `balanceAfter`

## 3. Business Constraints Enforced

1. Wallet belongs to exactly one user (`Wallet.userId`)
2. A user can own multiple wallets (`WalletRepository.findByUserId`)
3. Wallet balance cannot go negative (`Wallet.debit` checks)
4. Sufficient balance required before transfer
5. Self-transfer is blocked (`fromWalletId == toWalletId`)
6. Destination wallet cannot be `CLOSED`
7. Wallet cannot be closed with non-zero balance

## 4. Code Structure

### Application Entry
- `src/main/java/com/wraith/WalletServiceApplication.java`

### API Layer
- `src/main/java/com/wraith/controller/WalletController.java`

### Services
- Interfaces:
  - `src/main/java/com/wraith/service/WalletService.java`
  - `src/main/java/com/wraith/service/TransactionService.java`
- Implementations:
  - `src/main/java/com/wraith/service/impl/WalletServiceImpl.java`
  - `src/main/java/com/wraith/service/impl/TransactionServiceImpl.java`

### Domain Models
- `src/main/java/com/wraith/model/User.java`
- `src/main/java/com/wraith/model/Wallet.java`
- `src/main/java/com/wraith/model/Transaction.java`
- `src/main/java/com/wraith/model/WalletLedger.java`

### Repositories
- `src/main/java/com/wraith/repository/UserRepository.java`
- `src/main/java/com/wraith/repository/WalletRepository.java`
- `src/main/java/com/wraith/repository/TransactionRepository.java`
- `src/main/java/com/wraith/repository/WalletLedgerRepository.java`

### DTOs
- `src/main/java/com/wraith/dto/CreateWalletRequest.java`
- `src/main/java/com/wraith/dto/AddMoneyRequest.java`
- `src/main/java/com/wraith/dto/TransferFundsRequest.java`
- `src/main/java/com/wraith/dto/WalletDTO.java`
- `src/main/java/com/wraith/dto/TransactionDTO.java`

### Enums
- `src/main/java/com/wraith/enums/WalletStatus.java`
- `src/main/java/com/wraith/enums/TransactionType.java`
- `src/main/java/com/wraith/enums/TransactionStatus.java`

### Exceptions
- `src/main/java/com/wraith/exception/WalletException.java`
- `src/main/java/com/wraith/exception/WalletNotFoundException.java`
- `src/main/java/com/wraith/exception/InsufficientBalanceException.java`

## 5. Concurrency and Data Integrity

- `Wallet` uses optimistic locking via `@Version`
- Repository methods include pessimistic locking for critical flows
- Transfer flow acquires locks and runs within a transactional boundary
- Service methods use `@Transactional` for atomic write operations

## 6. API Contracts (Implemented)

Base URL: `/api/wallets`

### Wallet APIs
- `POST /api/wallets`
  - body: `{ userId, initialBalance }`
- `GET /api/wallets/{walletId}`
- `GET /api/wallets/{walletId}/balance`
- `GET /api/wallets/user/{userId}`
- `PUT /api/wallets/{walletId}/status?status=ACTIVE|INACTIVE|CLOSED`

### Transaction APIs
- `POST /api/wallets/{walletId}/add-money`
  - body: `{ amount, description }`
- `POST /api/wallets/transfer`
  - body: `{ fromWalletId, toWalletId, amount, description }`
- `GET /api/wallets/transactions/{transactionId}`
- `GET /api/wallets/{walletId}/transactions`
  - optional query params: `startDate`, `endDate`, `type`
- `GET /api/wallets/{walletId}/transactions/paginated?page=&size=`

`curl` examples are provided in `curl-commands.md`.

## 7. Configuration and Dependencies

### Build File
- `build.gradle`

### Main dependencies
- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-validation`
- `spring-boot-starter-actuator`
- `springdoc-openapi-starter-webmvc-ui`
- runtime DB drivers: PostgreSQL + H2
- test: JUnit5 + Spring Boot test

### App Config
- `src/main/resources/application.yml` (default PostgreSQL)
- `src/main/resources/application-prod.yml`

## 8. Testing Completed

Test suite files:
- `src/test/java/com/wraith/service/impl/WalletServiceImplTest.java`
- `src/test/java/com/wraith/service/impl/TransactionServiceImplTest.java`
- `src/test/java/com/wraith/controller/WalletControllerTest.java`
- `src/test/java/com/wraith/model/WalletTest.java`
- `src/test/java/com/wraith/model/EntityCallbacksTest.java`
- `src/test/java/com/wraith/dto/DtoValidationTest.java`
- `src/test/java/com/wraith/enums/EnumsTest.java`
- `src/test/java/com/wraith/exception/ExceptionClassesTest.java`
- `src/test/java/com/wraith/repository/RepositorySmokeTest.java`
- `src/test/java/com/wraith/WalletServiceApplicationTest.java`

## 9. DevOps/Packaging Artifacts Completed

- `Dockerfile`
- `docker-compose.yml`
- Helm chart:
  - `helm/wallet-service/Chart.yaml`
  - `helm/wallet-service/values.yaml`
  - `helm/wallet-service/templates/_helpers.tpl`
  - `helm/wallet-service/templates/secret.yaml`
  - `helm/wallet-service/templates/deployment.yaml`
  - `helm/wallet-service/templates/service.yaml`
  - `helm/wallet-service/templates/NOTES.txt`

## 10. How to Run

### Run tests

```bash
cd /Users/anuj/repository/machine-coding/wallet-service
./gradlew test
```

### Run application (default PostgreSQL)

```bash
cd /Users/anuj/repository/machine-coding/wallet-service
./gradlew bootRun
```

### Run with Docker Compose

```bash
cd /Users/anuj/repository/machine-coding/wallet-service
docker compose up --build
```

### Run with Helm

```bash
cd /Users/anuj/repository/machine-coding/wallet-service
helm install wallet-service ./helm/wallet-service
```

## 11. Completion Checklist

- [x] Wallet service implementation
- [x] Transaction service implementation
- [x] Constraints enforcement and validation
- [x] Ledger/audit handling
- [x] REST controller endpoints
- [x] Unit and integration-oriented tests
- [x] cURL command reference
- [x] README usage instructions
- [x] Docker + Compose files
- [x] Helm chart files

