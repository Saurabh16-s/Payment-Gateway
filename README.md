# PayFlow

A minimal payment orchestration engine built to explore the core infrastructure problems behind systems like Stripe — idempotent transaction processing, double-entry ledger accounting, and per-client rate limiting.

This is not a checkout integration. It's a from-scratch payment processor: the layer that sits *behind* a "Pay Now" button, responsible for making sure money moves correctly, safely, and exactly once.

## Why this exists

Payment systems are hard for reasons that aren't obvious until you build one:

- **Networks fail and retry.** A client can send the same payment request twice — a real double-charge risk unless the server can recognize a duplicate.
- **Balances must never be "just a number."** Storing an account balance as a single mutable column invites race conditions and makes it impossible to audit how a balance got to its current value.
- **Abuse happens.** Without rate limiting, a single bad actor (or bug) can hammer the payment API and degrade service for everyone else.

PayFlow implements solutions to all three, using the same patterns real payment processors rely on.

## Core features

### 1. Idempotency keys
Every `POST /payments` and `POST /payments/{id}/refund` request can include an `Idempotency-Key` header. If the same key is sent twice, the server returns the **original cached response** instead of processing the payment again — preventing accidental double-charges from client retries, network timeouts, or user double-clicks.

### 2. Double-entry ledger
Account balances are never stored directly. Instead, every transaction writes **two ledger entries** — a debit and a credit — and a balance is always *computed* as the sum of its entries. This is the same accounting model used by real financial systems: it's self-auditing, and it makes "where did this money go" always answerable by querying history rather than trusting a mutable field.

### 3. Per-client rate limiting
Implemented with [Bucket4j](https://github.com/bucket4j/bucket4j) (token bucket algorithm), scoped per API key. Protects the payment API from abuse or runaway retry loops without needing external infrastructure like Redis for a project at this scale.

## Tech stack

**Backend**
- Java 17, Spring Boot 3.5
- Spring Data JPA + Hibernate
- PostgreSQL (containerized via Docker Compose)
- Bucket4j for rate limiting
- Lombok

**Frontend**
- React
- Axios
- Plain CSS (no framework — kept intentionally lightweight)

## Architecture

```
Client
  │
  ▼
RateLimitFilter        (429 if over quota — checked first, before any DB work)
  │
  ▼
IdempotencyInterceptor (returns cached response if Idempotency-Key was seen before)
  │
  ▼
PaymentController → PaymentService → LedgerService (double-entry write, @Transactional)
  │
  ▼
PostgreSQL
```

## API endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/accounts` | Create an account |
| `GET` | `/accounts/{id}/balance` | Get computed balance from ledger |
| `POST` | `/accounts/{id}/fund` | Seed a test balance |
| `POST` | `/payments` | Create a payment (supports `Idempotency-Key` header) |
| `POST` | `/payments/{id}/refund` | Refund a payment, full or partial |

## Running locally

**1. Start PostgreSQL:**
```bash
cd backend
docker compose up -d
```

**2. Run the backend:**
```bash
cd backend
./mvnw spring-boot:run
```
Backend runs on `http://localhost:8085`.

**3. Run the frontend:**
```bash
cd frontend
npm install
npm start
```
Frontend runs on `http://localhost:3000`.

## Demoing idempotency

The frontend's payment form generates a fresh UUID as the idempotency key for each new payment. Click **"Retry (same key)"** after a successful payment — the amount won't be charged twice, because the server recognizes the key and returns the cached result instead of re-running the transaction.

## What's intentionally out of scope

This is a focused resume/learning project, not a production payment processor. Deliberately excluded:
- Webhooks / async event delivery
- A full payment state machine (authorize → capture → settle)
- Real card network integration
- Multi-currency support

The goal was depth on a few core correctness problems (idempotency, ledger integrity, rate limiting) rather than breadth of features.

## License

MIT
