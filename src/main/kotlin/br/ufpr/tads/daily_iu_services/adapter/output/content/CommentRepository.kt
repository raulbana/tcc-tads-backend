package br.ufpr.tads.daily_iu_services.adapter.output.content

import br.ufpr.tads.daily_iu_services.domain.entity.content.Comment
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {

    fun countByContentIdAndReplyFalse(contentId: Long): Int

    fun findByContentIdAndReplyFalse(contentId: Long, pageable: Pageable): List<Comment>

    fun findByReplies_Id(parentCommentId: Long, pageable: Pageable): List<Comment>

    fun findByReplyToComment_Id(parentCommentId: Long, pageable: Pageable): List<Comment>
}