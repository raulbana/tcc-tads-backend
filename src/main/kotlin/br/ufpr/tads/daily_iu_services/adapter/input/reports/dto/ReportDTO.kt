package br.ufpr.tads.daily_iu_services.adapter.input.reports.dto

import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.CalendarDayDTO
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ReportDTO(
    val user: ReportUserDTO,
    val history: List<CalendarDayDTO>,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val generatedAt: LocalDateTime
)
