package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO
import br.ufpr.tads.daily_iu_services.domain.validator.PasswordPattern
import jakarta.validation.Valid
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UserEditorDTO (
    val name: String?,
    @field:Email
    val email: String?,
    val profilePicture: MediaDTO?,
)
