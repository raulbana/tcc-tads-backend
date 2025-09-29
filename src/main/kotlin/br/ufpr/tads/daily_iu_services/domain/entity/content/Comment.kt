package br.ufpr.tads.daily_iu_services.domain.entity.content

import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import com.fasterxml.jackson.annotation.JsonIgnore
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
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
data class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "contentId")
    val content: Content,

    @OneToOne
    @JoinColumn(name = "authorId", referencedColumnName = "id")
    val author: User,

    var text: String,
    val reply: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "replyToCommentId")
    val replyToComment: Comment? = null,

    @OneToMany(mappedBy = "replyToComment")
    val replies: MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "comment", cascade = [CascadeType.ALL], orphanRemoval = true)
    val likes: MutableList<CommentLikes> = mutableListOf(),

    @Column(insertable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(insertable = false, updatable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
)