package br.ufpr.tads.daily_iu_services.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "`User`")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var isHighContrast: Boolean,

    @Column(nullable = false)
    var isBigFont: Boolean
)