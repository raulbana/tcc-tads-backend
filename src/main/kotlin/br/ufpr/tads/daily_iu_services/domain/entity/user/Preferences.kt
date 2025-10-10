package br.ufpr.tads.daily_iu_services.domain.entity.user

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalTime

@Entity
@Table(name = "preferences")
class Preferences (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var highContrast: Boolean,
    var bigFont: Boolean,
    var reminderCalendar: Boolean,
    var reminderCalendarSchedule: LocalTime?,
    var reminderWorkout: Boolean,
    var reminderWorkoutSchedule: LocalTime?,
    var encouragingMessages: Boolean,
    var workoutMediaType: String
)