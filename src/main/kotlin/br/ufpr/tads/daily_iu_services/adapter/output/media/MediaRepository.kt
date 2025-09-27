package br.ufpr.tads.daily_iu_services.adapter.output.media

import br.ufpr.tads.daily_iu_services.domain.entity.media.Media
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MediaRepository : JpaRepository<Media, Long> {
}