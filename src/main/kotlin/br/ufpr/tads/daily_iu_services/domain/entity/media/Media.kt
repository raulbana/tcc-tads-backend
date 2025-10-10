package br.ufpr.tads.daily_iu_services.domain.entity.media

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "media")
class Media(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val url: String,
    val contentType: String,
    val contentSize: Long,
    var altText: String,
    var createdAt: LocalDateTime
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Media) return false

        if (contentSize != other.contentSize) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contentSize.hashCode()
        result = 31 * result + url.hashCode()
        return result
    }
}