package br.ufpr.tads.daily_iu_services.adapter.input.questions.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class QuestionDTO (
    val id: String,
    val text: String,
    val type: String,
    val options: List<QuestionOptionDTO>?,
    val min: Int?,
    val max: Int?,
    val step: Int?,
    val required: Boolean?,
    val placeholder: String?,
    var hidden: Boolean = false,
    var answer: Any? = null
)