package br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalTime

data class NotificationsDTO(
    @field:NotNull
    val reminderCalendar: Boolean,
    val reminderCalendarSchedule: LocalTime?,

    @field:NotNull
    val reminderWorkout: Boolean,
    val reminderWorkoutSchedule: LocalTime?,

    @field:NotNull
    val encouragingMessages: Boolean,

    @field:NotNull
    val notificationToken: String
)
