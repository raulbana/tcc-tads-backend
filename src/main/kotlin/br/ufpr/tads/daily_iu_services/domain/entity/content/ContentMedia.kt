package br.ufpr.tads.daily_iu_services.domain.entity.content

import br.ufpr.tads.daily_iu_services.domain.entity.media.Media
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "contentMedia")
class ContentMedia(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "contentId", referencedColumnName = "id")
    val content: Content,

    @ManyToOne
    @JoinColumn(name = "mediaId", referencedColumnName = "id")
    val media: Media
)