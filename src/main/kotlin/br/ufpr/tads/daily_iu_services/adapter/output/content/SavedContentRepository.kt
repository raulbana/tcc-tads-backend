package br.ufpr.tads.daily_iu_services.adapter.output.content

import br.ufpr.tads.daily_iu_services.domain.entity.content.SavedContent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SavedContentRepository: JpaRepository<SavedContent, Long> {

    fun findByUserIdAndContent_VisibleTrue(userId: Long): List<SavedContent>

    fun findByUserIdAndContentId(userId: Long, contentId: Long): SavedContent?

    fun existsByUserIdAndContentId(userId: Long, contentId: Long): Boolean

    fun countByUserIdAndContent_StrikedFalse(userId: Long): Long?
}