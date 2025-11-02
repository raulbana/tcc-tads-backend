package br.ufpr.tads.daily_iu_services.adapter.input.admin.dto

import org.jetbrains.annotations.NotNull

data class ReportToggleDTO(
    @field:NotNull
    val contentId: Long,
    @field:NotNull
    val reportId: Long,
    @field:NotNull
    val valid: Boolean
)
