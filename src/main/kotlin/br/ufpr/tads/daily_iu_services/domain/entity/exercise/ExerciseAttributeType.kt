package br.ufpr.tads.daily_iu_services.domain.entity.exercise

enum class ExerciseAttributeType(val value: Int, val label: String) {
    BENEFIT(1, "BENEFIT"),
    CONTRAINDICATION(2, "CONTRAINDICATION"),
    UNKNOWN(99, "UNKNOWN");

    companion object {
        fun fromValue(value: Int): ExerciseAttributeType {
            return ExerciseAttributeType.entries.find { it.value == value } ?: UNKNOWN
        }

        fun fromLabel(label: String): ExerciseAttributeType {
            return ExerciseAttributeType.entries.find { it.label == label } ?: UNKNOWN
        }
    }
}