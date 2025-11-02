package br.ufpr.tads.daily_iu_services.adapter.input.questions.dto

import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.PatientProfileDTO
import br.ufpr.tads.daily_iu_services.adapter.input.user.dto.UserWorkoutPlanDTO

data class OnboardCompleteDTO(
    val profile: PatientProfileDTO,
    val workoutPlan: UserWorkoutPlanDTO?
)