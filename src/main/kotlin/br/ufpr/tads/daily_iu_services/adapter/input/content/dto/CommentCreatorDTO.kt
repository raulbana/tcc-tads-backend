package br.ufpr.tads.daily_iu_services.adapter.input.content.dto

data class CommentCreatorDTO(
    val contentId: Long,
    val authorId: Long,
    val text: String,
    val replyToCommentId: Long? = null
)