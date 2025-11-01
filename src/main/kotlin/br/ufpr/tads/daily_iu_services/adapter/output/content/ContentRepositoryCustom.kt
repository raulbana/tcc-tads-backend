package br.ufpr.tads.daily_iu_services.adapter.output.content

import br.ufpr.tads.daily_iu_services.domain.entity.content.Content
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ContentRepositoryCustom {
    fun findRecommendedByUserLikes(userId: Long, pageable: Pageable): Page<Content>
}
