package br.ufpr.tads.daily_iu_services.adapter.input.reports.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class ReportParamDTO(
    @field:NotNull(message = "Data inicial é obrigatória")
    val from: LocalDate,

    @field:NotNull(message = "Data final é obrigatória")
    val to: LocalDate
)