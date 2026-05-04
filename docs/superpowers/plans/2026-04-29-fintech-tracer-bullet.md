# Fintech Tracer Bullet (Phase 1) Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Establish a connectivity foundation between Next.js, FastAPI, and Supabase deployed to Cloud Run.

**Architecture:** Pure tracer bullet validating Auth flow and cross-service communication.

**Tech Stack:** Next.js (TS), FastAPI (Python), Supabase, Docker, Google Cloud Run, GitHub Actions.

---

### Task 1: Initialize Project Structure

**Files:**
- Create: `README.md`
- Create: `.gitignore`

- [ ] **Step 1: Create root README**
```markdown
# Fintech FIAP 2026
- `ui-dashboard`: Next.js Frontend
- `core-api`: FastAPI Backend
```
- [ ] **Step 2: Create root .gitignore**
```text
node_modules/
__pycache__/
.env
.next/
dist/
```
- [ ] **Step 3: Commit**
```bash
git add .
git commit -m "chore: init project structure"
```

### Task 2: Setup FastAPI Backend (core-api)

**Files:**
- Create: `core-api/main.py`
- Create: `core-api/requirements.txt`
- Create: `core-api/.env.example`

- [ ] **Step 1: Define requirements**
```text
fastapi
uvicorn
python-dotenv
```
- [ ] **Step 2: Create minimal FastAPI app**
```python
from fastapi import FastAPI

app = FastAPI()

@app.get("/health")
async def health():
    return {"status": "healthy"}
```
- [ ] **Step 3: Test locally**
Run: `cd core-api && pip install -r requirements.txt && uvicorn main:app --reload`
Expected: `curl http://localhost:8000/health` returns `{"status": "healthy"}`
- [ ] **Step 4: Commit**
```bash
git add core-api/
git commit -m "feat(core): init fastapi health check"
```

### Task 3: Setup Next.js Frontend (ui-dashboard)

**Files:**
- Create: `ui-dashboard/` (via create-next-app)
- Modify: `ui-dashboard/src/app/page.tsx`

- [ ] **Step 1: Initialize Next.js**
Run: `npx create-next-app@latest ui-dashboard --typescript --tailwind --eslint --app --src-dir --import-alias "@/*"`
- [ ] **Step 2: Basic Rose-Pine Scaffold**
```tsx
export default function Home() {
  return (
    <main className="min-h-screen bg-[#191724] text-[#e0def4] p-24">
      <h1 className="text-4xl font-bold text-[#ebbcba]">Furlan</h1>
      <p className="mt-4">Tracer Bullet: Connectivity Phase</p>
    </main>
  );
}
```
- [ ] **Step 3: Test locally**
Run: `cd ui-dashboard && npm run dev`
Expected: View Rose-Pine themed page at localhost:3000
- [ ] **Step 4: Commit**
```bash
git add ui-dashboard/
git commit -m "feat(ui): init next.js with rose-pine scaffold"
```

### Task 4: Integrate Supabase Auth

**Files:**
- Create: `ui-dashboard/src/lib/supabase.ts`
- Modify: `ui-dashboard/.env.local`

- [ ] **Step 1: Install client**
Run: `cd ui-dashboard && npm install @supabase/supabase-js`
- [ ] **Step 2: Configure Client**
```typescript
import { createClient } from '@supabase/supabase-js'
const supabaseUrl = process.env.NEXT_PUBLIC_SUPABASE_URL!
const supabaseAnonKey = process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY!
export const supabase = createClient(supabaseUrl, supabaseAnonKey)
```
- [ ] **Step 3: Verify local env setup**
Expected: `.env.local` contains placeholder keys.
- [ ] **Step 4: Commit**
```bash
git add ui-dashboard/src/lib/supabase.ts
git commit -m "feat(ui): add supabase client"
```

### Task 5: Dockerization (Tracer)

**Files:**
- Create: `core-api/Dockerfile`
- Create: `ui-dashboard/Dockerfile`

- [ ] **Step 1: core-api Dockerfile**
```dockerfile
FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt
COPY . .
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8080"]
```
- [ ] **Step 2: ui-dashboard Dockerfile (Standalone)**
```dockerfile
FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM node:18-alpine
WORKDIR /app
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package.json ./package.json
CMD ["npm", "start"]
```
- [ ] **Step 3: Commit**
```bash
git add .
git commit -m "chore: add dockerfiles for tracer services"
```
