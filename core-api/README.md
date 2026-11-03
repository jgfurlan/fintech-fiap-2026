# Fintech FIAP 2026 — AI / ML Microservice (`core-api`)

## 1. Overview & Architectural Role

`core-api` is the dedicated **Python AI / Machine Learning microservice** for the Fintech FIAP 2026 platform. It provides financial predictive analytics, proactive budget-velocity forecasting, anomaly/threat-level detection, and agentic recommendation synthesis.

In accordance with the project's **Coordinator-Worker-Delegator (CWD)** pattern:
- **Worker (`app/workers/`):** Pure deterministic computations for linear spending projections, category deviation calculations, and threat-level assessments (`good`, `warning`, `critical`, `overrun`).
- **Coordinator (`app/coordinator.py`):** Orchestrates data acquisition (seed data or Supabase), triggers analytical workers, and delegates to AI inference when threshold deviations require agentic guidance.
- **Delegator / AI Service (`app/services/bedrock_service.py`):** Integrates with **AWS Bedrock** (Claude 3.5 Sonnet) to generate personalized, explainable financial action plans and savings recommendations.

---

## 2. API Endpoints

All application routes adhere to FastAPI REST conventions with strict Pydantic model validation.

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| `GET` | `/api` | No | Service metadata, status, and API version. |
| `GET` | `/api/health` | No | System health check and upstream Supabase connectivity verification. |
| `GET` | `/api/me` | Yes (`Bearer <token>`) | Retrieves current authenticated user context from Supabase GoTrue. |
| `GET` | `/api/agent/budget-insight` | Yes (`Bearer <token>`) | Calculates predictive budget velocity and returns AI action items for the active user. |

### Sample Response (`GET /api/agent/budget-insight`)

```json
{
  "user_id": "seed-user-001",
  "report_date": "2026-05-15",
  "currency": "BRL",
  "projected_month_end_total": 925.00,
  "budget_total": 600.00,
  "deviation_pct": 0.54,
  "deviation_amount": 325.00,
  "threat_level": "overrun",
  "action_items": [
    {
      "action": "Skip one restaurant meal this week and prepare dinner at home.",
      "projected_savings": 85.00,
      "reason": "Dining spending is pacing 54% over monthly allocated budget."
    }
  ],
  "generated_at": "2026-09-29T03:40:00Z"
}
```

---

## 3. Configuration & Environment Variables

Copy `.env.example` to `.env` in `core-api/`:

```bash
cp .env.example .env
```

| Variable | Required | Description | Example |
|---|---|---|---|
| `SUPABASE_URL` | Yes | Supabase project URL | `https://xxxx.supabase.co` |
| `SUPABASE_ANON_KEY` | Yes | Supabase public anonymous API key | `eyJhbGciOi...` |
| `AWS_REGION` | Optional | AWS Region for Bedrock inference (default: `us-east-1`) | `us-east-1` |
| `AWS_ACCESS_KEY_ID` | Optional | AWS credentials for Bedrock Claude 3.5 Sonnet | `AKIA...` |
| `AWS_SECRET_ACCESS_KEY` | Optional | AWS secret key for Bedrock | `wJalrX...` |
| `GCP_PROJECT_ID` | Optional | Google Cloud Project ID (if deploying via GCP/Cloud Run) | `fintech-fiap-2026` |
| `PORT` | Optional | Server port (default: `8080` in container, `8000` local) | `8000` |

---

## 4. Local Development & Setup

### Prerequisites
- Python 3.12+ (or Python 3.14)
- `pip` or `uv`

### Installation

```bash
cd core-api

# Create and activate virtual environment
python3 -m venv .venv
source .venv/bin/activate

# Install dependencies
pip install -r requirements.txt
```

### Running the API

```bash
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

The interactive OpenAPI documentation is available at `http://localhost:8000/docs`.

---

## 5. Verification & Testing

The test suite covers:
- Direct dependency unit tests for `get_user` authentication and error handling (`tests/test_auth.py`).
- Endpoint integration tests for `/api`, `/api/health`, and `/api/me` (`tests/test_main.py`).
- AI/ML predictive analytics, linear projections, threat detection, Bedrock fallback, and `/api/agent/budget-insight` (`tests/test_agent.py`).

### Execute Tests

```bash
# Run with virtual environment pytest
.venv/bin/pytest -v
```

### Linting & Formatting

```bash
.venv/bin/ruff check .
.venv/bin/ruff format --check .
```

---

## 6. Containerization & Deployment

`core-api` includes an optimized Dockerfile for containerized deployment (e.g., AWS App Runner, ECS Fargate, or GCP Cloud Run).

```bash
# Build Docker image
docker build -t fintech-core-api:latest .

# Run container
docker run -p 8080:8080 --env-file .env fintech-core-api:latest
```
