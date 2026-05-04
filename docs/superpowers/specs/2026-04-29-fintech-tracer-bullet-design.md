# Spec: Fintech Tracer Bullet (Phase 1)
**Date:** 2026-04-29  
**Status:** Approved  
**Topic:** Infrastructure & Connectivity Foundation

## 1. Goal
Establish a "Pure Tracer Bullet" architecture. Validate the full deployment pipeline and connectivity between Next.js (UI), FastAPI (Core), and Supabase (Auth/DB) on Google Cloud Run.

## 2. Architecture
- **Frontend:** Next.js (TypeScript) + Rose-Pine Theme + Tailwind CSS.
- **Backend:** FastAPI (Python) + Pydantic.
- **Identity/Data:** Supabase (Auth & Postgres).
- **Deployment:** 
  - Containerization: Docker (Multi-stage builds).
  - Registry: Google Artifact Registry.
  - Hosting: Google Cloud Run.
  - CI/CD: GitHub Actions.

## 3. Success Criteria
1. UI renders basic Rose-Pine scaffold.
2. User can authenticate via Supabase.
3. Authenticated UI can fetch `/health` from FastAPI.
4. Deployment is fully automated on `git push`.

## 4. Approach (Option 1: Pure Tracer)
- **Step 1:** Initialize Next.js in `ui-dashboard`.
- **Step 2:** Initialize FastAPI in `core-api`.
- **Step 3:** Setup Supabase project and local env.
- **Step 4:** Write Dockerfiles for both services.
- **Step 5:** Configure GitHub Actions for Cloud Run.

## 5. Future Evolution (Self-Driving)
This foundation will later support Agentic AI by adding LangGraph nodes to the FastAPI backend and real-time streaming to the Next.js frontend.
