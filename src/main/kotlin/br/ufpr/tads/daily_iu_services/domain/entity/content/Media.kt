package br.ufpr.tads.daily_iu_services.domain.entity.content

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "media")
class Media(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val url: String,
    val contentType: String,
    val contentSize: Long,
    var description: String?,
    var altText: String,
    var caption: String?,
    val createdAt: String
)