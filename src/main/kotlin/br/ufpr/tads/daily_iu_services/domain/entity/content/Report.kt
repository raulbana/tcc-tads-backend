package br.ufpr.tads.daily_iu_services.domain.entity.content

import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "report")
data class Report(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "contentId", referencedColumnName = "id")
    val content: Content,

    @ManyToOne
    @JoinColumn(name = "reportedByUserId", referencedColumnName = "id")
    val reportedByUser: User,

    val reason: String,

    var valid: Boolean = true,

    var handled: Boolean = false,

    @Column(insertable = false, updatable = false)
    val createdAt: LocalDateTime
)
