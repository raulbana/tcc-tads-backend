package br.ufpr.tads.daily_iu_services.adapter.input.content.dto

data class CommentDTO(
    val id: Long,
    val author: AuthorDTO,
    val text: String,
    val createdAt: String,
    val updatedAt: String,
    val likesCount: Int,
    val isLiked: Boolean,
    val repliesCount: Int
)