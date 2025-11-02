package br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto

data class WorkoutPlanDTO(
    val id: Long?,
    val name: String,
    val description: String,
    val workouts: Map<Int, WorkoutDTO>,
    val daysPerWeek: Int,
    val totalWeeks: Int,
    val iciqScoreMin: Int,
    val iciqScoreMax: Int,
    val ageMin: Int,
    val ageMax: Int
)
