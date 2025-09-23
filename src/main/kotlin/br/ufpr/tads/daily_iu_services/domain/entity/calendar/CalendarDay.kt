package br.ufpr.tads.daily_iu_services.domain.entity.calendar

import jakarta.persistence.*

@Entity
@Table(name = "calendarDay")
class CalendarDay(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "dateValue")
    val date: String,
    val userId: Long,

    @Enumerated(EnumType.STRING)
    var leakageLevel: LeakageLevel,

    var eventsCount: Int,
    var completedExercises: Int,
    var notesPreview: String?,
    val dayTitle: String
)