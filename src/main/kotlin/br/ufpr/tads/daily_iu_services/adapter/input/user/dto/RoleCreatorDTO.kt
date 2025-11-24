package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

import org.jetbrains.annotations.NotNull

data class RoleCreatorDTO (

    @field:NotNull
    val description: String,

    @field:NotNull
    val permissionLevel: Int,

    @field:NotNull
    val reason: String,

    @field:NotNull
    val documentType: String?,

    @field:NotNull
    val documentValue: String?
)