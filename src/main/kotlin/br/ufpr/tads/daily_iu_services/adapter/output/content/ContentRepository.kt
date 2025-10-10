package br.ufpr.tads.daily_iu_services.adapter.output.content

import br.ufpr.tads.daily_iu_services.domain.entity.content.Content
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContentRepository : JpaRepository<Content, Long> {
}