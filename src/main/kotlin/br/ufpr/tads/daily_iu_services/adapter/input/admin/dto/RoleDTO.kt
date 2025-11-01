package br.ufpr.tads.daily_iu_services.adapter.input.admin.dto

import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class RoleDTO(
    val description: String,
    val permissionLevel: Int,
    val reason: String,
    val hasDocument: Boolean,
    val documentType: String?,
    val documentValue: String?,
    val conceivedBy: User?,

    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val conceivedAt: LocalDateTime
)
