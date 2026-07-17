# Decision log

## Decision 1: LC state machine

Version 1 starts when the Letter of Credit has already been issued. The application and credit-approval stages are outside the scope of this version.

When the seller presents the documents, the LC moves from ISSUED to UNDER_EXAMINATION.

If the documents match the LC terms, the LC moves to COMPLIANT.

If the documents do not match, the LC moves to DISCREPANT.

The buyer can accept the mismatch by waiving the discrepancy. This moves the LC from DISCREPANT to COMPLIANT.

If the buyer refuses the documents, the LC moves to REFUSED.

If the expiry date passes before the required documents are presented, a scheduled job publishes LcExpired and the LC moves to EXPIRED.

After successful settlement, the LC moves from COMPLIANT to SETTLED.

Decision Log

This file records the important architecture and domain decisions made while building the LC Flow platform.

Each decision should explain what was decided, why it was chosen, and any important limitations or trade-offs.

⸻

Decision 1: Letter of Credit State Machine

Context

A Letter of Credit is a bank’s conditional promise to pay the seller, provided that the documents presented by the seller match the terms defined in the Letter of Credit.

The platform needs a clear state machine so that every service understands:

* the current status of a Letter of Credit;
* which transitions are allowed;
* which event causes each transition;
* which service produces each event;
* and which states are terminal.

Scope

Version 1 of the platform starts when the Letter of Credit is issued.

The following stages are outside the scope of version 1:

* LC application submission;
* buyer credit assessment;
* credit-limit approval;
* sanctions screening;
* compliance approval;
* and human approval before issuance.

These stages may be added in a later version.

The first state in version 1 is therefore ISSUED.

LC creation

When the LC issuance service successfully creates and issues a Letter of Credit, it publishes the LcIssued event.

LcIssued is the birth event of the Letter of Credit aggregate.

The event is produced by the LC issuance service.

Main states

The Letter of Credit can exist in the following states:

* ISSUED
* UNDER_EXAMINATION
* COMPLIANT
* DISCREPANT
* SETTLED
* REFUSED
* EXPIRED

State transitions

LC issued

When the LC issuance service creates the Letter of Credit, it publishes:

LcIssued

The Letter of Credit enters the ISSUED state.

Documents presented

When the seller presents the required documents, the document-presentation service publishes:

DocumentsPresented

The Letter of Credit moves from:

ISSUED → UNDER_EXAMINATION

Documents compliant

If all presented documents match the terms of the Letter of Credit, the document-presentation service publishes:

DocumentsCompliant

The Letter of Credit moves from:

UNDER_EXAMINATION → COMPLIANT

Discrepancy raised

If one or more documents do not match the terms of the Letter of Credit, the document-presentation service publishes:

DiscrepancyRaised

The Letter of Credit moves from:

UNDER_EXAMINATION → DISCREPANT

Discrepancy waived

The buyer may decide to accept the documents despite the mismatch.

The document-presentation service records the buyer’s decision and publishes:

DiscrepancyWaived

The Letter of Credit moves from:

DISCREPANT → COMPLIANT

A discrepancy waiver does not change the original terms of the Letter of Credit. It only records that the buyer accepts the presented documents despite the discrepancy.

Documents refused

If the buyer does not accept the discrepancy, the document-presentation service publishes:

DocumentsRefused

The Letter of Credit moves from:

DISCREPANT → REFUSED

Settlement completed

When payment has been completed successfully, the settlement service publishes:

SettlementCompleted

The Letter of Credit moves from:

COMPLIANT → SETTLED

LC amended

The terms of an issued Letter of Credit may be changed through an amendment.

Examples include:

* changing the shipment date;
* changing the expiry date;
* changing the quantity;
* changing the amount;
* or changing required document conditions.

The LC issuance service publishes:

LcAmended

This is a self-transition:

ISSUED → ISSUED

The state remains ISSUED, but the LC version and terms are updated.

An amendment is different from a discrepancy waiver.

An amendment changes the terms of the Letter of Credit.

A waiver accepts a document mismatch without changing the Letter of Credit terms.

LC expired

A scheduled job in the LC issuance service periodically checks for issued Letters of Credit whose expiry dates have passed.

When an LC expires, the scheduler publishes:

LcExpired

The Letter of Credit moves from:

ISSUED → EXPIRED

Time-based changes are represented as domain events in the same way as user-triggered changes.

Event producers

Event	Producing service
LcIssued	LC issuance service
LcAmended	LC issuance service
LcExpired	Scheduler inside the LC issuance service
DocumentsPresented	Document-presentation service
DocumentsCompliant	Document-presentation service
DiscrepancyRaised	Document-presentation service
DiscrepancyWaived	Document-presentation service
DocumentsRefused	Document-presentation service
SettlementCompleted	Settlement service

Transition table

Current state	Event	Next state
No existing LC	LcIssued	ISSUED
ISSUED	DocumentsPresented	UNDER_EXAMINATION
ISSUED	LcAmended	ISSUED
ISSUED	LcExpired	EXPIRED
UNDER_EXAMINATION	DocumentsCompliant	COMPLIANT
UNDER_EXAMINATION	DiscrepancyRaised	DISCREPANT
DISCREPANT	DiscrepancyWaived	COMPLIANT
DISCREPANT	DocumentsRefused	REFUSED
COMPLIANT	SettlementCompleted	SETTLED

Terminal states

The following states are terminal:

* SETTLED
* REFUSED
* EXPIRED

Once a Letter of Credit reaches a terminal state, it cannot move back to a previous state.

If a problem is discovered after settlement, such as fraud or a forged document, the platform must not reverse the LC state.

Instead, a new business process should begin using new events, such as:

* FraudCaseOpened
* RefundInitiated
* RecoveryStarted

State machines move forward. Corrections are represented by new events and compensating actions rather than by rewriting history.

Decision

The platform will use an event-driven state machine with explicit states, valid transitions, named domain events, and clearly identified event producers.

Invalid state transitions must be rejected by the service that owns the Letter of Credit state.

Every successful state change should be persisted and published as a domain event.
