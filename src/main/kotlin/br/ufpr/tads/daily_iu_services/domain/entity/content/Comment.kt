package br.ufpr.tads.daily_iu_services.domain.entity.content

import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "comment")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "contentId", referencedColumnName = "id", insertable = false, updatable = false)
    val content: Content,

    @OneToOne
    @JoinColumn(name = "authorId", referencedColumnName = "id")
    val author: User,

    var text: String,
    val reply: Boolean,

    @OneToOne
    @JoinColumn(name = "replyToCommentId", referencedColumnName = "id")
    val replyToComment: Comment?,

    @OneToMany(mappedBy = "comment", cascade = [CascadeType.ALL], orphanRemoval = true)
    val likes: List<CommentLikes>,

    @Column(insertable = false, updatable = false)
    val createdAt: String
)