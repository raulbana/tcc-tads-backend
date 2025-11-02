package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

data class UserDTO(
    val id: Long? = null,
    val name: String,
    val email: String,
    val profilePictureUrl: String?,
    val role: String,
    val profile: PatientProfileDTO?,
    val preferences: PreferencesDTO
)