package br.ufpr.tads.daily_iu_services.domain.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ValidDateImpl : ConstraintValidator<ValidDate, LocalDate> {
    private var required = true

    companion object {
        val OUTPUT_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    override fun initialize(constraintAnnotation: ValidDate) {
        super.initialize(constraintAnnotation)
        this.required = constraintAnnotation.required
    }

    override fun isValid(input: LocalDate?, context: ConstraintValidatorContext): Boolean {
        if (input == null) {
            return !this.required
        }

        try {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate(input.format(OUTPUT_FORMAT)).addConstraintViolation()
            return true
        } catch (_: DateTimeParseException) {
            return false
        }
    }
}