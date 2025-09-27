package br.ufpr.tads.daily_iu_services.adapter.input.reports.dto

import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.CalendarDayDTO
import java.time.LocalDateTime

data class ReportDTO(
    val user: ReportUserDTO,
    val history: List<CalendarDayDTO>,
    val generatedAt: LocalDateTime = LocalDateTime.now()
)
