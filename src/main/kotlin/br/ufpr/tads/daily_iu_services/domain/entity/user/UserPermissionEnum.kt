package br.ufpr.tads.daily_iu_services.domain.entity.user

enum class UserPermissionEnum(val level: Int, val description: String) {
    COMMON(1, "Usuário comum"),
    PROFESSIONAL(2, "Profissional de saúde"),
    ADMIN(3, "Administrador")
}