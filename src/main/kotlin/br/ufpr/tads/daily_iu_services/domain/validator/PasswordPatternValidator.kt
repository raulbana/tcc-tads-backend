package br.ufpr.tads.daily_iu_services.domain.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PasswordPatternValidator : ConstraintValidator<PasswordPattern, String> {

    override fun isValid(password: String?, p1: ConstraintValidatorContext?): Boolean {
        if (password == null) return false
        val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")
        return passwordPattern.containsMatchIn(password)
    }
}