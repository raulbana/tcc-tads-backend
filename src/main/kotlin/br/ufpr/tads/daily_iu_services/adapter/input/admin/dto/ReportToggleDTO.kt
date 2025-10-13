package br.ufpr.tads.daily_iu_services.adapter.input.admin.dto

data class ReportToggleDTO(
    val contentId: Long,
    val reportId: Long,
    val valid: Boolean
)
