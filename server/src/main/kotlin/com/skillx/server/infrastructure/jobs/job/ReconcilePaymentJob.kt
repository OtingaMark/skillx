package com.skillx.server.infrastructure.jobs.job

import com.skillx.server.features.payments.application.usecase.HandleStripeWebhookUseCase
import com.skillx.server.features.payments.application.usecase.VerifyPurchaseUseCase
import org.jobrunr.jobs.annotations.Job
import org.jobrunr.jobs.context.JobContext

class ReconcilePaymentJob(
    private val verifyPurchaseUseCase: VerifyPurchaseUseCase,
    private val handleStripeWebhookUseCase: HandleStripeWebhookUseCase
) {

    @Job(name = "Reconcile payment: %{transactionId}")
    fun execute(transactionId: String, context: JobContext) {
        // Reconcile payment with Stripe/RevenueCat
    }

    @Job(name = "Reconcile all pending payments")
    fun executeBatch(context: JobContext) {
        // Batch reconciliation logic
    }
}