package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

import br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto.WorkoutPlanDTO
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class UserWorkoutPlanDTO(
    val id: Long?,
    val plan: WorkoutPlanDTO,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val startDate: LocalDateTime,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val endDate: LocalDateTime?,
    val totalProgress: Int,
    val weekProgress: Int,
    val currentWeek: Int,
    val nextWorkout: Int?,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val lastWorkoutDate: LocalDateTime?,
    val completed: Boolean
)