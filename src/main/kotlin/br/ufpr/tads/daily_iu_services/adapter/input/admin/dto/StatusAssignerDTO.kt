package br.ufpr.tads.daily_iu_services.adapter.input.admin.dto

import org.jetbrains.annotations.NotNull

data class StatusAssignerDTO(
    @field:NotNull
    val targetUserId: Long,

    @field:NotNull
    val blocked: Boolean
)
