package br.ufpr.tads.daily_iu_services.domain.entity.question

enum class QuestionTypeEnum(val value: String) {
    TEXT("text"),
    RADIO("radio"),
    CHECKBOX("checkbox"),
    SLIDER("slider"),
    SELECT("select"),
    DATE("date"),
    TIME("time"),
    NUMBER("number");

    override fun toString(): String {
        return value
    }
}