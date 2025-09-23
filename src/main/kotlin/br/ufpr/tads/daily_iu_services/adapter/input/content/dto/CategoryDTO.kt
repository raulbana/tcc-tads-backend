package br.ufpr.tads.daily_iu_services.adapter.input.content.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull

data class CategoryDTO (
    val id: Long? = null,

    @field:NotNull
    val name: String,

    @field:NotEmpty
    @field:Size(max = 255, message = "A descrição não deve exceder 255 caracteres")
    var description: String,

    @field:NotNull
    var auditable: Boolean,

    val createdAt: String?
)