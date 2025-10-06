package br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.CalendarDayDTO
import br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.ReportDTO
import br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.ReportUserDTO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ReportMapper {
    fun reportToReportDTO(
        userDTO: ReportUserDTO,
        history: List<CalendarDayDTO>
    ): ReportDTO = ReportDTO(
        user = userDTO,
        history = history,
        generatedAt = LocalDateTime.now()
    )
}