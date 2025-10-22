package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

data class ExerciseFeedbackCreatorDTO(
    val exerciseId: Long,
    val workoutId: Long,
    val rating: Int,
    val evaluation: String,
    val comments: String?
)