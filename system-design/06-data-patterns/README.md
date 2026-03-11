# Phase 2: Data Patterns

## Overview

This module connects CAP theorem theory to real data-layer decisions you make in production systems.

**Duration**: Week 3-4 (Days 23-24)  
**Prerequisites**: Communication patterns, database fundamentals  
**Goal**: Choose the right consistency and replication strategy per use case

---

## Primary Deep Dive

### [CAP-THEOREM.md](./CAP-THEOREM.md)

Includes:
- CAP theorem foundations (CP vs AP)
- Consistency models (strong, eventual, causal)
- Quorum design (W + R > N)
- Conflict resolution (LWW, vector clocks, CRDTs)
- Decision framework with real-world examples

---

## Practical Data Engineering Patterns

### 1) Consistency by domain boundary

Do not pick one consistency model for the whole system:
- **Money, inventory, identity**: strong consistency paths
- **Feeds, counters, analytics**: eventual consistency paths
- **Hybrid systems**: strict writes + async fan-out reads

### 2) Data contract evolution

For events and APIs:
- Version schemas
- Add backward-compatible fields first
- Keep consumers tolerant to unknown fields
- Track schema usage before removing old fields

### 3) Recovery-first replication design

Before choosing replication topology, define:
- RPO (how much data loss is acceptable)
- RTO (how quickly service must recover)
- Region failover ownership and steps

---

## 10x Practical Checklist

- [ ] Mapped each data domain to a consistency model
- [ ] Defined quorum strategy and failure behavior
- [ ] Documented conflict resolution policy
- [ ] Added schema evolution strategy for APIs/events
- [ ] Defined backup, restore, and failover runbook
- [ ] Added data quality checks (freshness, completeness, correctness)

---

## Practice Drills

1. **Partition drill**: Simulate a network split and decide fail-open vs fail-closed per endpoint.
2. **Conflict drill**: Reconcile concurrent updates for cart, profile, and inventory.
3. **Recovery drill**: Execute a table restore from backup and measure RTO.

---

## Self-Assessment

- [ ] I can justify CP vs AP per business workflow
- [ ] I can explain quorum math with concrete numbers
- [ ] I can design conflict resolution beyond last-write-wins
- [ ] I can define a safe schema migration sequence
- [ ] I can explain recovery strategy using RPO/RTO

---

## Next Module

[07-distributed-systems](../07-distributed-systems/README.md) - Consensus, transactions, and distributed failure handling

---

**Remember**: The best data design is the one that preserves business correctness under failure, not just when everything is healthy.
