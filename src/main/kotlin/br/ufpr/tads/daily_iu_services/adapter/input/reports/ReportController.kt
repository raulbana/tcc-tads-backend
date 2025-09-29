package br.ufpr.tads.daily_iu_services.adapter.input.reports

import br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.ReportDTO
import br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.ReportParamDTO
import br.ufpr.tads.daily_iu_services.domain.service.ReportService
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/report")
class ReportController(val service: ReportService) {
    @GetMapping
    fun getReport(
        @RequestHeader("x-user-id") @Min(1) userId: Long,
        @Valid @ModelAttribute params: ReportParamDTO
    ): ResponseEntity<ReportDTO> {
        return ResponseEntity.ok(service.getReport(userId, params))
    }
}