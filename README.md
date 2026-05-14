# Fintech FIAP 2026

Full-stack fintech monorepo built for FIAP's post-grad AI course. Phase 1 (Tracer Bullet) validates end-to-end deployment to Google Cloud Run using Python FastAPI + React.

## Quick Start

```bash
# 1. Copy env files
cp .env.example .env
cp core-api/.env.example core-api/.env
# Fill in your Supabase credentials

# 2. Start everything
docker compose up --build

# 3. Open
#   UI:    http://localhost:3000
#   API:   http://localhost:8080/api
#   Docs:  http://localhost:8080/docs (Swagger UI)
```

## Architecture

```
┌─────────────┐      ┌─────────────┐      ┌─────────────┐
│ ui-dashboard│──────▶│  core-api   │──────▶│  Supabase   │
│  (React)    │      │  (FastAPI)  │      │  (Auth/DB)  │
└─────────────┘      └─────────────┘      └─────────────┘
      │                       │
      ▼                       ▼
  Cloud Run              Cloud Run
  (Frontend)             (Backend)
```

| Layer | Tech | Purpose |
|-------|------|---------|
| Frontend | React + TypeScript + Tailwind CSS | Dashboard UI |
| Backend | FastAPI + Pydantic | REST API |
| Auth & DB | Supabase | JWT auth, user management |
| Deploy | Docker + GCR + Cloud Run | Containerized hosting |
| CI/CD | GitHub Actions | Automated build & deploy |

## Project Structure

```
.
├── .github/workflows/      # CI/CD pipelines
├── core-api/               # FastAPI backend (Phase 1)
│   ├── app/
│   │   ├── main.py         # API routes (health, me, root)
│   │   └── models/api.py   # Pydantic response models
│   ├── tests/              # pytest suite
│   ├── Dockerfile
│   └── requirements.txt
├── ui-dashboard/           # React frontend (Phase 1)
│   ├── src/
│   │   ├── App.tsx         # Main app (auth, health display)
│   │   └── lib/supabase.ts # Supabase client
│   ├── Dockerfile
│   └── nginx.conf
├── assets/                 # Design assets & research
├── docs/                   # Documentation
├── issues/                 # Issue tracking (ADR-lite)
└── docker-compose.yml      # Local orchestration
```

## System Requirements

- Docker & Docker Compose
- Node.js 20+ (for local frontend dev)
- Python 3.12+ (for local backend dev)
- Supabase project (free tier works)
- GCP project with Cloud Run API enabled

## Environment Setup

Create `.env` at the project root and `core-api/.env`:

```
SUPABASE_URL=https://your-project.supabase.co
SUPABASE_ANON_KEY=your-anon-key
```

For the UI dashboard, create `ui-dashboard/.env`:

```
REACT_APP_SUPABASE_URL=https://your-project.supabase.co
REACT_APP_SUPABASE_ANON_KEY=your-anon-key
REACT_APP_API_URL=http://localhost:8080
```

## Development

### Backend only

```bash
cd core-api
python -m venv .venv
source .venv/bin/activate  # or .venv\Scripts\activate on Windows
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8080
```

### Frontend only

```bash
cd ui-dashboard
npm install
npm start
```

### Run tests

```bash
# Backend
cd core-api
pytest

# Frontend
cd ui-dashboard
npm test
```

## API Endpoints

| Method | Path | Description | Auth |
|--------|------|-------------|------|
| GET | `/api/health` | Health check with Supabase status | No |
| GET | `/api` | Root message + version | No |
| GET | `/api/me` | Current user info | Yes (Bearer token) |

OpenAPI docs available at `/docs` when running locally.

## Deployment

The project deploys to Google Cloud Run via GitHub Actions on every push to `main`.

See `.github/workflows/deploy.yml` for details.

## Phase Roadmap

| Phase | Dates | Goal | Status |
|-------|-------|------|--------|
| **1: Tracer Bullet** | May-Jun | Validate full pipeline end-to-end | In Progress |
| **2: Migration to Java** | Jul-Aug | Rewrite backend to Spring Boot | Planned |
| **3: AI Agent** | Sep-Oct | Add LangGraph microservice | Planned |
| **4: Polish & Deploy** | Nov | Production hardening | Planned |

## Contributing

1. Create a branch: `git checkout -b feature/description`
2. Make changes with tests
3. Run the test suite locally
4. Open a PR against `main`
5. CI will auto-deploy on merge

## License

MIT
