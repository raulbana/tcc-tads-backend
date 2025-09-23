package br.ufpr.tads.daily_iu_services.adapter.output.calendar

import br.ufpr.tads.daily_iu_services.domain.entity.calendar.CalendarDay
import br.ufpr.tads.daily_iu_services.domain.entity.calendar.UrinationData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UrinationDataRepository : JpaRepository<UrinationData, Long> {

    fun findBycalendarDay(calendarDay: CalendarDay): List<UrinationData>?
}