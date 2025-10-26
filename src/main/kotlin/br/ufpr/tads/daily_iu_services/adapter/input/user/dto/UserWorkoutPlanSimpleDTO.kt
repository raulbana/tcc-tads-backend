package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class UserWorkoutPlanSimpleDTO (
    val id: Long?,
    val plan: String,
    val description: String,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val startDate: LocalDateTime,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val endDate: LocalDateTime?,
    val totalProgress: Int,
    val weekProgress: Int,
    val currentWeek: Int,
    val completed: Boolean
)
