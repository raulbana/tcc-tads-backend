package br.ufpr.tads.daily_iu_services.adapter.input.content.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

data class ContentCategoryDTO (
    val id: Long? = null,

    @field:NotNull
    val name: String,

    @field:NotEmpty
    @field:Size(max = 255, message = "A descrição não deve exceder 255 caracteres")
    var description: String,

    @field:NotNull
    var auditable: Boolean,

    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime?
)