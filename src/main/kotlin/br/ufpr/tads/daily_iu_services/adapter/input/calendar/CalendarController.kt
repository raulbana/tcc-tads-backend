package br.ufpr.tads.daily_iu_services.adapter.input.calendar

import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.CalendarDayDTO
import br.ufpr.tads.daily_iu_services.adapter.input.calendar.dto.CalendarRequestDTO
import br.ufpr.tads.daily_iu_services.domain.service.CalendarService
import br.ufpr.tads.daily_iu_services.domain.validator.ValidDate
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/calendar")
class CalendarController(private val calendarService: CalendarService) {

    @GetMapping
    fun getCalendarEvents(
        @RequestParam(required = false) @ValidDate(required = false) from: String?,
        @RequestParam(required = false) @ValidDate(required = false) to: String?,
        @RequestHeader(value = "user-id") userId: Long
    ): ResponseEntity<HashMap<String, CalendarDayDTO>> {
        return ResponseEntity.ok(calendarService.getCalendarEvents(userId, from, to))
    }

    @PutMapping
    fun setCalendarEvent(
        @RequestBody @Valid request: CalendarRequestDTO,
        @RequestHeader(value = "user-id") userId: Long
    ): ResponseEntity<CalendarDayDTO> {
        return ResponseEntity.ok(calendarService.createOrUpdateEvent(userId, request))
    }
}