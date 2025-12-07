package br.ufpr.tads.daily_iu_services.adapter.output.content

import br.ufpr.tads.daily_iu_services.domain.entity.content.Content
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository

interface ContentRepository : JpaRepository<Content, Long>, ContentRepositoryCustom {
    fun findByIdAndStrikedFalse(id: Long): Content?

    fun findByAuthorIdAndStrikedFalse(authorId: Long, pageable: PageRequest): List<Content>

    fun findByVisibleFalseAndStrikedFalse(pageable: PageRequest): List<Content>

    fun countByAuthorIdAndStrikedFalse(userId: Long): Long

    fun countTotalLikesByAuthorIdAndStrikedFalse(userId: Long): Long?
}