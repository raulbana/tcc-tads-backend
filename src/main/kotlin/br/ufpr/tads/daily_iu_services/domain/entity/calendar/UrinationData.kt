package br.ufpr.tads.daily_iu_services.domain.entity.calendar

import jakarta.persistence.*
import net.minidev.json.annotate.JsonIgnore
import java.sql.Time

@Entity
@Table(name = "urinationData")
class UrinationData(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "timeValue")
    val time: Time,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendarDayId")
    var calendarDay: CalendarDay? = null,

    @Enumerated(EnumType.STRING)
    val amount: LeakageLevel,

    val leakage: Boolean,
    val reason: String?,
    val urgency: Boolean
)