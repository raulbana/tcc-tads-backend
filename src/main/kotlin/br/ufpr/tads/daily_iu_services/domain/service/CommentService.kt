package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.CommentCreatorDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.CommentDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.ToggleDTO
import br.ufpr.tads.daily_iu_services.adapter.input.content.dto.mapper.ContentMapper
import br.ufpr.tads.daily_iu_services.adapter.output.content.CommentRepository
import br.ufpr.tads.daily_iu_services.adapter.output.content.ContentRepository
import br.ufpr.tads.daily_iu_services.adapter.output.user.UserRepository
import br.ufpr.tads.daily_iu_services.domain.entity.content.Comment
import br.ufpr.tads.daily_iu_services.domain.entity.content.CommentLikes
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val contentRepository: ContentRepository,
    private val userRepository: UserRepository
) {

    fun getCommentsByContentId(contentId: Long, userId: Long, page: Int?): List<CommentDTO> {
        val pageable = Pageable.ofSize(15).withPage(page ?: 0)
        val comments = commentRepository.findByContentIdAndReplyFalse(contentId, pageable)

        return comments.map { ContentMapper.INSTANCE.commentToDTO(it, userId) }
    }

    fun getRepliesByCommentId(commentId: Long, userId: Long, page: Int?): List<CommentDTO> {
        val pageable = Pageable.ofSize(15).withPage(page ?: 0)
        val comments = commentRepository.findByReplyToComment_Id(commentId, pageable)

        return comments.map { ContentMapper.INSTANCE.commentToDTO(it, userId) }
    }

    fun createComment(request: CommentCreatorDTO): CommentDTO{
        val savedComment = if (request.replyToCommentId != null) {
            val parentComment = commentRepository.findById(request.replyToCommentId).orElseThrow {
                throw NotFoundException("Comentário com id ${request.replyToCommentId} não encontrado")
            }
            val content = parentComment.content
            val author = userRepository.findById(request.authorId).orElseThrow {
                throw NotFoundException("Usuário com id ${request.authorId} não encontrado")
            }

            val reply = Comment(
                content = content,
                author = author,
                text = request.text,
                reply = true,
                replyToComment = parentComment
            )

            // Salva o reply antes de associar ao parent
            val savedReply = commentRepository.save(reply)
            parentComment.replies.add(savedReply)
            commentRepository.save(parentComment)

            // Retorna o reply salvo
            savedReply
        } else {
            val content = contentRepository.findByIdAndStrikedFalse(request.contentId) ?:
                throw NotFoundException("Conteúdo com id ${request.contentId} não encontrado")

            val author = userRepository.findById(request.authorId).orElseThrow {
                throw NotFoundException("Usuário com id ${request.authorId} não encontrado")
            }

            val newComment = Comment(
                content = content,
                author = author,
                text = request.text,
                reply = false
            )
            commentRepository.save(newComment)
        }
        return ContentMapper.INSTANCE.commentToDTO(savedComment, request.authorId)
    }

    fun updateComment(commentId: Long, request: CommentCreatorDTO, userId: Long): CommentDTO {
        val existingComment = commentRepository.findById(commentId).orElseThrow {
            throw NotFoundException("Comentário com id $commentId não encontrado")
        }

        if (existingComment.author.id != userId) {
            throw IllegalAccessException("Usuário com id $userId não é o autor do comentário")
        }

        val updatedComment = existingComment.copy(text = request.text)

        val savedComment = commentRepository.save(updatedComment)
        return ContentMapper.INSTANCE.commentToDTO(savedComment, userId)
    }

    fun toggleLikeComment(commentId: Long, toggleDTO: ToggleDTO) {
        val comment = commentRepository.findById(commentId).orElseThrow {
            throw NotFoundException("Comentário com id $commentId não encontrado")
        }

        if (toggleDTO.control) {
            val alreadyLiked = comment.likes.any { it.userId == toggleDTO.userId }
            if (!alreadyLiked) {
                comment.likes.add(CommentLikes(userId = toggleDTO.userId, comment = comment))
                commentRepository.save(comment)
            }
        } else {
            val like = comment.likes.find { it.userId == toggleDTO.userId }
            if (like != null) {
                comment.likes.remove(like)
                commentRepository.save(comment)
            }
        }
    }

    fun deleteComment(commentId: Long) {
        val comment = commentRepository.findById(commentId).orElseThrow {
            throw NotFoundException("Comentário com id $commentId não encontrado")
        }

        if (comment.replies.isNotEmpty()) commentRepository.deleteAll(comment.replies)

        commentRepository.delete(comment)
    }
}