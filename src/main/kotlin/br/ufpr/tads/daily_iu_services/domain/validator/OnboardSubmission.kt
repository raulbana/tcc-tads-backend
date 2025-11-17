package br.ufpr.tads.daily_iu_services.domain.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [OnboardSubmissionValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class OnboardSubmission(
    val message: String = "Perguntas obrigatórias ausentes ou em branco",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)