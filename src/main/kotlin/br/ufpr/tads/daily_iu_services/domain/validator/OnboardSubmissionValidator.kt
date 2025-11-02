package br.ufpr.tads.daily_iu_services.domain.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class OnboardSubmissionValidator : ConstraintValidator<OnboardSubmission, Map<String, String>> {

    private val requiredQuestions = setOf(
        "birthdate",
        "q3_frequency",
        "q4_amount",
        "q5_interference"
    )

    override fun isValid(answers: Map<String, String>?, context: ConstraintValidatorContext?): Boolean {
        if (answers == null) {
            buildViolation(context, "Map de respostas é nulo")
            return false
        }

        val missingOrBlank = requiredQuestions.filter { key -> answers[key]?.isBlank() ?: true }

        if (missingOrBlank.isEmpty()) return true

        val message = "Perguntas obrigatórias ausentes ou em branco: ${missingOrBlank.joinToString(", ")}"
        buildViolation(context, message)
        return false
    }

    private fun buildViolation(context: ConstraintValidatorContext?, message: String) {
        context?.disableDefaultConstraintViolation()
        context?.buildConstraintViolationWithTemplate(message)?.addConstraintViolation()
    }
}