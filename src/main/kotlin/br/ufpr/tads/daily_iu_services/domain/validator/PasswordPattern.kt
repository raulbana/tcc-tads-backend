package br.ufpr.tads.daily_iu_services.domain.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.annotation.Retention
import kotlin.annotation.Target
import kotlin.reflect.KClass

@Constraint(validatedBy = [PasswordPatternValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class PasswordPattern(
    val message: String = "A senha deve conter ao menos 8 caracteres, incluindo uma letra maiúscula, uma letra minúscula, um número e um caractere especial.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
