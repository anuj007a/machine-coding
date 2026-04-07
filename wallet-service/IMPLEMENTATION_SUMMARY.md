# Wallet Service - Implementation Summary

## 1) Overview

`wallet-service` is a Spring Boot 3.2.5 application for wallet and transaction lifecycle management.

Core responsibilities:
- Create and manage wallets per user
- Add money and transfer funds between wallets
- Enforce wallet safety and balance rules
- Persist transaction and ledger history
- Expose REST APIs with validation and OpenAPI docs

## 2) Tech Stack

- Java 21
- Spring Boot (`web`, `data-jpa`, `validation`, `actuator`)
- PostgreSQL (default runtime datasource)
- H2 (runtime dependency, usable via runtime override)
- Lombok
- Springdoc OpenAPI UI
- JUnit 5 + Spring Boot Test + Mockito

## 3) Package Structure

- `com.wraith.controller` - API layer (`WalletController`)
- `com.wraith.service` - Service interfaces (`WalletService`, `TransactionService`)
- `com.wraith.service.impl` - Business implementations
- `com.wraith.model` - JPA entities (`Wallet`, `Transaction`, `WalletLedger`, `User`)
- `com.wraith.repository` - Spring Data JPA repositories
- `com.wraith.dto` - Request/response DTOs
- `com.wraith.enums` - Domain enums
- `com.wraith.exception` - Domain exceptions

## 4) Business Rules Implemented

- A wallet belongs to exactly one user (`userId` in `Wallet`)
- User can have multiple wallets (`findByUserId` support)
- Wallet balance cannot go negative (guard in `Wallet.debit`)
- Transfer validates sufficient balance
- Self-transfer is blocked
- Closed destination wallet cannot receive transfer
- Wallet close blocked when balance is non-zero
- Every successful money movement writes ledger entries with:
  - `balanceBefore`
  - `balanceAfter`

## 5) API Endpoints

Base path: `/api/wallets`

Wallet operations:
- `POST /api/wallets` - create wallet
- `GET /api/wallets/{walletId}` - wallet details
- `GET /api/wallets/{walletId}/balance` - current balance
- `GET /api/wallets/user/{userId}` - wallets for a user
- `PUT /api/wallets/{walletId}/status?status=ACTIVE|INACTIVE|CLOSED` - update status

Transaction operations:
- `POST /api/wallets/{walletId}/add-money` - credit wallet
- `POST /api/wallets/transfer` - transfer funds
- `GET /api/wallets/transactions/{transactionId}` - transaction details
- `GET /api/wallets/{walletId}/transactions` - list transactions (optional date/type filters)
- `GET /api/wallets/{walletId}/transactions/paginated?page=&size=` - paginated list

## 6) Validation and Error Handling

DTO validation:
- `CreateWalletRequest`: `userId` required, non-negative initial balance
- `AddMoneyRequest`: positive amount
- `TransferFundsRequest`: from/to wallet IDs required, positive amount

Exceptions:
- `WalletException`
- `WalletNotFoundException`
- `InsufficientBalanceException`

## 7) Concurrency and Consistency

- Optimistic locking in `Wallet` via `@Version`
- Pessimistic locking in repositories for critical updates and transfer paths
- Service methods wrapped in `@Transactional` boundaries

## 8) Testing Coverage Added

Test classes include:
- Service tests:
  - `WalletServiceImplTest`
  - `TransactionServiceImplTest`
- Controller test:
  - `WalletControllerTest` (`@WebMvcTest`)
- Model tests:
  - `WalletTest`
  - `EntityCallbacksTest`
- Supporting tests:
  - `DtoValidationTest`
  - `EnumsTest`
  - `ExceptionClassesTest`
  - `RepositorySmokeTest`
  - `WalletServiceApplicationTest`

## 9) Operational Artifacts

- `curl-commands.md` - API command catalog
- `Dockerfile` - multi-stage image build
- `docker-compose.yml` - app + PostgreSQL local stack
- Helm chart at `helm/wallet-service`:
  - `Chart.yaml`
  - `values.yaml`
  - templates for Deployment/Service/Secret/helpers

## 10) Notes

- Default `application.yml` points to PostgreSQL (`wallet_db`)
- For local DB-less run, use H2 override args documented in `README.md`
- OpenAPI docs path: `/v3/api-docs`, UI: `/swagger-ui.html`

