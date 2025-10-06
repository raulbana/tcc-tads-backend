package br.ufpr.tads.daily_iu_services.domain.entity.calendar

import jakarta.persistence.*
import java.sql.Time

@Entity
@Table(name = "urinationData")
class UrinationData(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "timeValue")
    val time: Time,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendarDayId")
    var calendarDay: CalendarDay? = null,

    @Enumerated(EnumType.STRING)
    val amount: LeakageLevel,

    val leakage: Boolean,
    val reason: String?,
    val urgency: Boolean?
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UrinationData) return false

        if (leakage != other.leakage) return false
        if (urgency != other.urgency) return false
        if (time != other.time) return false
        if (amount != other.amount) return false
        if (reason != other.reason) return false

        return true
    }

    override fun hashCode(): Int {
        var result = leakage.hashCode()
        result = 31 * result + (urgency?.hashCode() ?: 0)
        result = 31 * result + time.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + (reason?.hashCode() ?: 0)
        return result
    }
}