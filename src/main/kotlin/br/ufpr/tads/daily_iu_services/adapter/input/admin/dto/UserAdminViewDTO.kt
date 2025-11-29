package br.ufpr.tads.daily_iu_services.adapter.input.admin.dto

data class UserAdminViewDTO (
    val id: Long? = null,
    val name: String,
    val email: String,
    val role: RoleDTO,
    val status: String,
)
