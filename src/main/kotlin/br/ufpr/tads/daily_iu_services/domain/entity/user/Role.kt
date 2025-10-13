package br.ufpr.tads.daily_iu_services.domain.entity.user

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
@Table(name = "role")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var description: String,
    var permissionLevel: Int,
    var reason: String,
    var hasDocument: Boolean = false,
    var documentType: String? = null,
    var documentValue: String? = null,

    @ManyToOne
    @JoinColumn(name = "conceivedBy", referencedColumnName = "id")
    var conceivedBy: User?,

    @Column(insertable = false, updatable = false)
    var conceivedAt: LocalDateTime
)