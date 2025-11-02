package br.ufpr.tads.daily_iu_services.domain.entity.calendar

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "calendarDay")
class CalendarDay(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "dateValue")
    val date: LocalDate,
    val userId: Long,

    @Enumerated(EnumType.STRING)
    var leakageLevel: LeakageLevel,

    @OneToMany(
        mappedBy = "calendarDay",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    val urinationData: MutableList<UrinationData> = mutableListOf(),

    var eventsCount: Int,
    var completedExercises: Int,
    var notesPreview: String?,
    val dayTitle: String
)