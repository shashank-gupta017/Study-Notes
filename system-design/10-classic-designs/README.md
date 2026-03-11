# Phase 4: Classic System Designs

## Overview

This module turns core patterns into complete designs you can explain in interviews and harden for production.

**Duration**: Week 7-8  
**Prerequisites**: Fundamentals through Security & Privacy  
**Goal**: Deliver end-to-end designs with clear trade-offs and operational readiness

---

## Included Deep Dives

### [URL-SHORTENER.md](./URL-SHORTENER.md)
- End-to-end design for high-read traffic
- ID generation and encoding strategies
- Caching, redirects, analytics pipeline

### [TWITTER.md](./TWITTER.md)
- Fan-out trade-offs (write vs read vs hybrid)
- Timeline generation architecture
- Sharding, caching, and hot-key handling

---

## Interview-to-Production 2-Pass Method

### Pass 1: Interview Design (45 minutes)
- Clarify requirements
- Estimate capacity
- Design APIs + schema
- Draw architecture
- Discuss bottlenecks and trade-offs

### Pass 2: Production Hardening (45 minutes)
- Define SLOs and error budget
- Add failure-mode analysis and fallback behavior
- Add rollout plan (canary, feature flag, rollback)
- Add observability (metrics, logs, traces, alerts)
- Add cost model and optimization levers

---

## Production Hardening Checklist

- [ ] SLOs defined for critical user journeys
- [ ] Single points of failure identified and mitigated
- [ ] Backpressure/rate-limit behavior documented
- [ ] Safe rollout and rollback plan documented
- [ ] On-call runbook created for top 3 incidents
- [ ] Cost hotspots identified with guardrails

---

## Suggested Next Designs (from workbook)

After these two deep dives, practice:
1. Key-Value Store
2. Rate Limiter
3. Notification System
4. API Gateway
5. Chat Application
6. Payment System

Use the same 2-pass method for each.

---

## Self-Assessment

- [ ] I can complete a clean design in 45 minutes
- [ ] I can harden the same design for production
- [ ] I can explain failure handling and SLO trade-offs
- [ ] I can justify cost/performance trade-offs clearly
- [ ] I can communicate decisions at Staff+ depth

---

## Next Module

[11-advanced-designs](../11-advanced-designs/README.md) - Tackle complex, multi-domain systems

---

**Remember**: Strong engineers design for happy paths. 10x engineers design for failure, operations, and long-term evolution.
