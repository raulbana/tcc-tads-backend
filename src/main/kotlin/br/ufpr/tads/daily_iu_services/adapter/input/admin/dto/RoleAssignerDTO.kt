package br.ufpr.tads.daily_iu_services.adapter.input.admin.dto

import jakarta.validation.constraints.AssertTrue
import org.jetbrains.annotations.NotNull

data class RoleAssignerDTO(
    @field:NotNull
    val targetUserId: Long,

    @field:NotNull
    val description: String,

    @field:NotNull
    val permissionLevel: Int,

    @field:NotNull
    val reason: String,

    @field:NotNull
    val hasDocument: Boolean,

    val documentType: String?,

    val documentValue: String?
) {
    @AssertTrue(message = "O documento deve ser fornecido quando 'hasDocument' for verdadeiro e deve ser nulo caso contrário.")
    fun isDocumentInfoValid(): Boolean {
        return if (hasDocument) {
            !documentType.isNullOrBlank() && !documentValue.isNullOrBlank()
        } else {
            documentType.isNullOrBlank() && documentValue.isNullOrBlank()
        }
    }

    @AssertTrue(message = "Para níveis de permissão maiores que 1, um documento deve ser fornecido.")
    fun isDocumentRequiredForPermissionLevel(): Boolean {
        return if (permissionLevel > 1) {
            hasDocument && !documentType.isNullOrBlank() && !documentValue.isNullOrBlank()
        } else {
            true
        }
    }
}
