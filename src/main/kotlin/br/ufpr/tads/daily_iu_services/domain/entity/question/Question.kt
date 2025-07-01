package br.ufpr.tads.daily_iu_services.domain.entity.question

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collection = "questions")
class Question(
    @MongoId
    val id: String,
    val text: String,
    val type: QuestionTypeEnum,
    val options: List<QuestionOption>?,
    val min: Int?,
    val max: Int?,
    val step: Int?,
    val labels: List<String>?,
    val required: Boolean?,
    val placeholder: String?
) {
}