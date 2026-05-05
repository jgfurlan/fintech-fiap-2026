# Fix CI/CD Workflow Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Fix the CI/CD workflow in `.github/workflows/deploy.yml` by replacing the invalid `google-github-services/submit-steps-build@v1` action with standard Docker build + push and using the official Cloud Run deploy action.

**Architecture:** Transition from a custom (broken) build action to standard `docker/build-push-action` which is more robust and widely used. This involves setting up Buildx, configuring Docker auth for Google Artifact Registry, and using `google-github-actions/deploy-cloudrun` for deployment.

**Tech Stack:** GitHub Actions, Docker, Google Cloud Run, Google Artifact Registry.

---

### Task 1: Create Feature Branch

- [ ] **Step 1: Create and switch to `feature/cicd-fix` branch**

Run: `git checkout -b feature/cicd-fix`

---

### Task 2: Update `deploy-core-api` Job

**Files:**
- Modify: `.github/workflows/deploy.yml`

- [ ] **Step 1: Replace build and deploy steps in `deploy-core-api`**

Update the job to include:
- `docker/setup-buildx-action@v3`
- `gcloud auth configure-docker`
- `docker/build-push-action@v5`
- `google-github-actions/deploy-cloudrun@v2`

```yaml
  deploy-core-api:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - id: auth
        uses: google-github-actions/auth@v2
        with:
          credentials_json: ${{ secrets.GCP_SA_KEY }}
      - uses: google-github-actions/setup-gcloud@v2
      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v3
      - name: Authorize Docker
        run: gcloud auth configure-docker ${{ env.REGION }}-docker.pkg.dev
      - name: Build and Push Container
        uses: docker/build-push-action@v5
        with:
          context: ./core-api
          push: true
          tags: ${{ env.REGION }}-docker.pkg.dev/${{ env.PROJECT_ID }}/fintech/core-api:latest
      - name: Deploy to Cloud Run
        uses: google-github-actions/deploy-cloudrun@v2
        with:
          service: core-api
          image: ${{ env.REGION }}-docker.pkg.dev/${{ env.PROJECT_ID }}/fintech/core-api:latest
          region: ${{ env.REGION }}
          flags: "--allow-unauthenticated --port 8000"
```

---

### Task 3: Update `deploy-ui-dashboard` Job

**Files:**
- Modify: `.github/workflows/deploy.yml`

- [ ] **Step 1: Replace build and deploy steps in `deploy-ui-dashboard`**

```yaml
  deploy-ui-dashboard:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - id: auth
        uses: google-github-actions/auth@v2
        with:
          credentials_json: ${{ secrets.GCP_SA_KEY }}
      - uses: google-github-actions/setup-gcloud@v2
      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v3
      - name: Authorize Docker
        run: gcloud auth configure-docker ${{ env.REGION }}-docker.pkg.dev
      - name: Build and Push Container
        uses: docker/build-push-action@v5
        with:
          context: ./ui-dashboard
          push: true
          tags: ${{ env.REGION }}-docker.pkg.dev/${{ env.PROJECT_ID }}/fintech/ui-dashboard:latest
      - name: Deploy to Cloud Run
        uses: google-github-actions/deploy-cloudrun@v2
        with:
          service: ui-dashboard
          image: ${{ env.REGION }}-docker.pkg.dev/${{ env.PROJECT_ID }}/fintech/ui-dashboard:latest
          region: ${{ env.REGION }}
          flags: "--allow-unauthenticated --port 80"
```

---

### Task 4: Verification and Commit

- [ ] **Step 1: Run `action-validator` or similar if available, or just check syntax manually**
- [ ] **Step 2: Commit changes**

Run: `git add .github/workflows/deploy.yml`
Run: `git commit -m "fix(ci): replace invalid build action with docker/build-push-action and use official cloud run deploy action"`

- [ ] **Step 3: Push branch (optional based on user preference, but task says "commit directly" or branch)**
