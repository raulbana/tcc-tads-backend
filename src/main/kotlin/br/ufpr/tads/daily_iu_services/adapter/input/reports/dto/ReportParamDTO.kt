package br.ufpr.tads.daily_iu_services.adapter.input.reports.dto

import jakarta.validation.constraints.NotEmpty
import java.time.LocalDate

data class ReportParamDTO(
    @NotEmpty
    val startDate: LocalDate,

    @NotEmpty
    val endDate: LocalDate
)
