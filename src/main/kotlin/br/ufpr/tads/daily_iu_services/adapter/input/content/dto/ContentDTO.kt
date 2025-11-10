package br.ufpr.tads.daily_iu_services.adapter.input.content.dto

import br.ufpr.tads.daily_iu_services.adapter.input.media.dto.MediaDTO
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContentDTO(
    val id: Long,
    val title: String,
    val description: String,
    val subtitle: String?,
    val subcontent: String?,
    val categories: List<String>,
    val author: AuthorDTO,
    val cover: MediaDTO?,
    val media: List<MediaDTO>,
    val isSaved: Boolean,
    val isLiked: Boolean,
    val likesCount: Int,
    val comments: List<CommentDTO>,
    val commentsCount: Int,
    val isReposted: Boolean,
    val repostedFromContentId: Long?,
    val repostedByUser: AuthorDTO?,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime
)