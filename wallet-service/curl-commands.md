# Wallet Service cURL Commands

Base URL:

```bash
export BASE_URL="http://localhost:8080"
```

## 0) Precondition: create a user row in DB

`create wallet` requires an existing `userId`, and there is currently no user-creation API endpoint.
Use PostgreSQL directly (example):

```bash
export USER_ID="11111111-1111-1111-1111-111111111111"
psql "postgresql://postgres:postgres_Login@25@localhost:5432/wallet_db" -c "INSERT INTO users(user_id, email, name, created_at, updated_at) VALUES ('$USER_ID','demo@example.com','Demo User', now(), now()) ON CONFLICT DO NOTHING;"
```

## 1) Create wallet

```bash
curl -X POST "$BASE_URL/api/wallets" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "'$USER_ID'",
    "initialBalance": 1000.0
  }'
```

## 2) Get wallet details by ID

```bash
export WALLET_ID="<wallet-id>"
curl -X GET "$BASE_URL/api/wallets/$WALLET_ID"
```

## 3) Check wallet balance

```bash
curl -X GET "$BASE_URL/api/wallets/$WALLET_ID/balance"
```

## 4) Add money

```bash
curl -X POST "$BASE_URL/api/wallets/$WALLET_ID/add-money" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 250.0,
    "description": "Wallet top-up"
  }'
```

## 5) Deactivate / close wallet

Deactivate:

```bash
curl -X PUT "$BASE_URL/api/wallets/$WALLET_ID/status?status=INACTIVE"
```

Close:

```bash
curl -X PUT "$BASE_URL/api/wallets/$WALLET_ID/status?status=CLOSED"
```

## 6) Transfer funds between wallets

```bash
export FROM_WALLET_ID="<from-wallet-id>"
export TO_WALLET_ID="<to-wallet-id>"

curl -X POST "$BASE_URL/api/wallets/transfer" \
  -H "Content-Type: application/json" \
  -d '{
    "fromWalletId": "'$FROM_WALLET_ID'",
    "toWalletId": "'$TO_WALLET_ID'",
    "amount": 150.0,
    "description": "Transfer for order #1001"
  }'
```

## 7) Get transaction details by transaction ID

```bash
export TXN_ID="<transaction-id>"
curl -X GET "$BASE_URL/api/wallets/transactions/$TXN_ID"
```

## 8) List transactions for a wallet

All:

```bash
curl -X GET "$BASE_URL/api/wallets/$WALLET_ID/transactions"
```

By type:

```bash
curl -X GET "$BASE_URL/api/wallets/$WALLET_ID/transactions?type=CREDIT"
```

By date range (ISO-8601):

```bash
curl -G "$BASE_URL/api/wallets/$WALLET_ID/transactions" \
  --data-urlencode "startDate=2026-04-01T00:00:00" \
  --data-urlencode "endDate=2026-04-30T23:59:59"
```

Paginated:

```bash
curl -X GET "$BASE_URL/api/wallets/$WALLET_ID/transactions/paginated?page=0&size=10"
```

