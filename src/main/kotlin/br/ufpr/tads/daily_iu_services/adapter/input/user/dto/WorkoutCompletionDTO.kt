package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class WorkoutCompletionDTO(
    val workoutId : Long,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val completedAt: LocalDateTime
)
