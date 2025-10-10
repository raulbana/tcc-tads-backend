package br.ufpr.tads.daily_iu_services.domain.entity.calendar

enum class LeakageLevel(private val value: String) {
    NONE("NONE"),
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH");

    companion object {
        fun from(value: String): LeakageLevel {
            return entries.find { it.value == value } ?: NONE
        }
    }

    override fun toString(): String {
        return value
    }
}