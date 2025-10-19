package br.ufpr.tads.daily_iu_services.adapter.input.content.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class CommentDTO(
    val id: Long,
    val author: AuthorDTO,
    val text: String,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime,
    val likesCount: Int,
    val isLiked: Boolean,
    val repliesCount: Int
)