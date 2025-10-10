package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

data class UserDTO(
    val id: Long? = null,
    var name: String,
    var email: String,
    var profilePictureUrl: String?,
    val profile: PatientProfileDTO,
    val preferences: PreferencesDTO
)