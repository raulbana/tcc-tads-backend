package br.ufpr.tads.daily_iu_services.domain.entity.content

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "commentLikes")
class CommentLikes(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val userId: Long,

    @ManyToOne
    @JoinColumn(name = "commentId", referencedColumnName = "id", insertable = false, updatable = false)
    val comment: Comment,
)