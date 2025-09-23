package br.ufpr.tads.daily_iu_services.adapter.output.calendar

import br.ufpr.tads.daily_iu_services.domain.entity.calendar.CalendarDay
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CalendarRepository : JpaRepository<CalendarDay, Long> {

    @Query("SELECT * FROM CalendarDay c WHERE MONTH(c.dateValue) = :month AND YEAR(c.dateValue) = :year AND c.userId = :userId", nativeQuery = true)
    fun findByMonthAndUserId(@Param("month") month: Int, @Param("year") year: Int, @Param("userId") userId: Long): List<CalendarDay>

    @Query("SELECT c FROM CalendarDay c WHERE c.date BETWEEN :startDate AND :endDate AND c.userId = :userId")
    fun findByDateRangeAndUserId(@Param("startDate") startDate: String, @Param("endDate") endDate: String, @Param("userId") userId: Long): List<CalendarDay>

    fun findByDateAndUserId(date: String, userId: Long): CalendarDay?
}