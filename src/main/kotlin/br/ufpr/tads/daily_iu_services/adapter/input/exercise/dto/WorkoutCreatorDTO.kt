package br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto

import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

data class WorkoutCreatorDTO(
    @field:NotBlank
    val name: String,
    val description: String,
    val totalDuration: Int,
    @field:NotBlank
    val difficultyLevel: String,
    @field:NotNull
    val exerciseIds: Map<Int, Long>
)
