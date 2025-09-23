package br.ufpr.tads.daily_iu_services.domain.entity.content

import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "content")
class Content(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var title: String,
    var description: String,
    var subtitle: String?,
    var subcontent: String?,

    @OneToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    var category: Category,

    @OneToOne
    @JoinColumn(name = "authorId", referencedColumnName = "id")
    val author: User,

    val respost: Boolean,
    val respostFromContentId: Long?,
    val respostByAuthorId: Long?,

    @OneToMany(mappedBy = "content", cascade = [CascadeType.ALL], orphanRemoval = true)
    val likes: List<ContentLikes>,

    @OneToMany(mappedBy = "content", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: List<Comment>,

    val createdAt: String,
    val updatedAt: String
)