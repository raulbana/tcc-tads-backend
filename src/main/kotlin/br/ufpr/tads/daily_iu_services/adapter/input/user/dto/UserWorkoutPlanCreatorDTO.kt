package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

import com.fasterxml.jackson.annotation.JsonFormat
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

data class UserWorkoutPlanCreatorDTO (
    @field:NotNull
    val planId: Long,

    @field:NotNull
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val startDate: LocalDateTime,

    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val endDate: LocalDateTime?,

    @field:NotNull
    val totalProgress: Int,

    @field:NotNull
    val weekProgress: Int,

    @field:NotNull
    val currentWeek: Int,

    @field:NotNull
    val completed: Boolean
)
