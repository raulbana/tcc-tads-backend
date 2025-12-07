package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

import java.time.LocalTime

data class PreferencesDTO(
    val highContrast: Boolean,
    val bigFont: Boolean,
    val darkMode: Boolean,
    val reminderCalendar: Boolean,
    val reminderCalendarSchedule: LocalTime?,
    val reminderWorkout: Boolean,
    val reminderWorkoutSchedule: LocalTime?,
    val encouragingMessages: Boolean,
    var notificationToken: String = ""
)