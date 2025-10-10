package br.ufpr.tads.daily_iu_services.domain.entity.content

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "contentLikes")
class ContentLikes(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val userId: Long,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "contentId", referencedColumnName = "id")
    val content: Content,
)