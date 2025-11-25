package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

data class UserSimpleDTO (
    val id: Long? = null,
    val name: String,
    val email: String,
    val role: String,
    val profilePictureUrl: String?,
    val curtidas: Long = 0,
    val salvos: Long = 0,
    val postagens: Long = 0
)