package br.ufpr.tads.daily_iu_services.adapter.input.questions.dto

import br.ufpr.tads.daily_iu_services.domain.validator.OnboardSubmission
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "DTO para envio das respostas do questionário de onboarding",
    example = """
    {
        "userId": 1,
        "answers": {
            "birthdate": "1990-05-15",
            "q3_frequency": "3",
            "q4_amount": "4",
            "q5_interference": "5"
        }
    }
    """
)
data class OnboardSubmitDTO(
    val userId: Long?,

    @field:OnboardSubmission
    val answers: Map<String, String>
)