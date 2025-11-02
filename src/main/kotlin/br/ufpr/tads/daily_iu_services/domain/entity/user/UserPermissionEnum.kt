package br.ufpr.tads.daily_iu_services.domain.entity.user

enum class UserPermissionEnum(val level: Int, val label: String) {
    COMMON(1, "USER"),
    PROFESSIONAL(2, "PROFESSIONAL"),
    ADMIN(3, "ADMIN");

    companion object {
        fun fromLevel(level: Int): UserPermissionEnum {
            return UserPermissionEnum.entries.find { it.level == level } ?: COMMON
        }
    }
}