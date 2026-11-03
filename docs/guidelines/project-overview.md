# Project Overview: Fintech FIAP 2026

## Mission
Develop a state-of-the-art, full-stack fintech platform deployed to AWS App Runner, serving as a "Gym for Agents" to validate the synthesis of Autonomy and Empathy in financial services.

## High-Level User Flows
1. **Onboarding:** User authenticates via Supabase, establishing a "Trust Anchor."
2. **Account Management:** User views proactive "Anticipatory" balance and transaction insights.
3. **Agentic Interaction:** User interacts with specialized agents (Coordinator-Worker-Delegator) for budget planning and fraud detection.

## Success Criteria Benchmarks
- **Verifiable Outcomes:** All agent tasks must return a numerical reward (RLVR).
- **Agent-Legibility:** The codebase must be globally unique and unambiguous for LLM navigation.
- **Accessibility:** Meet WCAG 2.2 AA standards.
- **Performance:** AWS App Runner deployments must be stable with automated CI/CD.

## Out-of-Scope (Phase 1)
- Billing and enterprise-scale subscription structures.
- Direct banking integration (using mock external APIs for now).
- Full Next.js SSR (using CRA for the Tracer Bullet).
- Spring Boot/Java migration (deferred to Phase 2).

## Tech Stack
- **Frontend:** React (TypeScript, CRA)
- **Backend:** FastAPI (Python)
- **Auth/DB:** Supabase
- **Inference:** AWS Bedrock
- **Deployment:** Docker + Amazon ECR + AWS App Runner
- **Knowledge Base:** Graphify Knowledge Graph