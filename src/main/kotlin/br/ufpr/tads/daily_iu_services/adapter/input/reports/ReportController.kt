package br.ufpr.tads.daily_iu_services.adapter.input.reports

import br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.ReportDTO
import br.ufpr.tads.daily_iu_services.adapter.input.reports.dto.ReportParamDTO
import br.ufpr.tads.daily_iu_services.domain.service.ReportService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/report")
class ReportController(val service: ReportService) {
    @GetMapping("/{id}")
    fun getReport(
        @PathVariable id: Long,
        @RequestParam params: ReportParamDTO
    ): ResponseEntity<ReportDTO> {
        return ResponseEntity.ok(service.getReport(id, params))
    }
}