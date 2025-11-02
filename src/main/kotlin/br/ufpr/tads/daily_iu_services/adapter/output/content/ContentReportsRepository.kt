package br.ufpr.tads.daily_iu_services.adapter.output.content

import br.ufpr.tads.daily_iu_services.domain.entity.content.Report
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContentReportsRepository: JpaRepository<Report, Long> {

    fun countByContentIdAndValidTrue(contentId: Long): Long

    fun findByContentIdAndHandledFalse(contentId: Long): List<Report>

    fun existsByContentIdAndReportedByUserIdAndValidTrue(contentId: Long, userId: Long): Boolean

}