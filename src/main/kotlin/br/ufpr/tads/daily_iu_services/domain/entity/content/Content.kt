package br.ufpr.tads.daily_iu_services.domain.entity.content

import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import jakarta.persistence.CascadeType
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
@Table(name = "content")
data class Content(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var title: String,
    var description: String,
    var subtitle: String?,
    var subcontent: String?,

    @OneToMany(mappedBy = "content", cascade = [CascadeType.ALL], orphanRemoval = true)
    val categories: MutableList<ContentContentCategory> = mutableListOf(),

    @ManyToOne
    @JoinColumn(name = "authorId", referencedColumnName = "id")
    val author: User,

    val repost: Boolean = false,
    val repostFromContentId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "repostByAuthorId", referencedColumnName = "id")
    val repostByAuthor: User? = null,

    @OneToMany(mappedBy = "content", cascade = [CascadeType.ALL], orphanRemoval = true)
    val likes: MutableList<ContentLikes> = mutableListOf(),

    @OneToMany(mappedBy = "content", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "content", cascade = [CascadeType.ALL], orphanRemoval = true)
    val media: MutableList<ContentMedia> = mutableListOf(),

    var visible: Boolean = true,
    var striked: Boolean = false,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime = LocalDateTime.now()
)