package br.ufpr.tads.daily_iu_services.domain.entity.user

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "OTP")
class OTP (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    val user: User,

    var code: String,

    @Transient
    var auxCode: String = "",

    var expirationTime: Long,
    var used: Boolean = false
)