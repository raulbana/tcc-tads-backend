package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO
import br.ufpr.tads.daily_iu_services.domain.validator.PasswordPattern
import jakarta.validation.Valid
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UserCreatorDTO (
    @field:NotBlank
    val name: String,

    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotBlank
    @field:PasswordPattern
    val password: String,

    @field:Valid
    val profile: PatientProfileDTO?,

    @field:Valid
    val role: RoleCreatorDTO?,

    val profilePicture: MediaDTO?,

    val preferences: PreferencesDTO?,

    @field:Valid
    val workoutPlan: UserWorkoutPlanCreatorDTO?
) {
    @AssertTrue(message = "O perfil do paciente é obrigatório para usuários com a role default (nível de permissão 1).")
    fun isPatientProfileValid(): Boolean {
        if (role == null || role.permissionLevel == 1) {
            return profile != null
        }
        return true
    }
}
