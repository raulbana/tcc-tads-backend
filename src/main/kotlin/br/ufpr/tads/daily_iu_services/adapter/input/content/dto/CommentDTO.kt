package br.ufpr.tads.daily_iu_services.adapter.input.content.dto

import java.time.LocalDateTime

data class CommentDTO(
    val id: Long,
    val author: AuthorDTO,
    val text: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val likesCount: Int,
    val isLiked: Boolean,
    val repliesCount: Int
)