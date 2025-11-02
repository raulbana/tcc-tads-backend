package br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class WorkoutDTO(
    val id: Long?,
    val name: String,
    val description: String,
    val totalDuration: Int,
    val difficultyLevel: String,
    val exercises: Map<Int, ExerciseDTO>,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime
)