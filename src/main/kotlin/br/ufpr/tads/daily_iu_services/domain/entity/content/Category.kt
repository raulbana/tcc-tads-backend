package br.ufpr.tads.daily_iu_services.domain.entity.content

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "contentCategory")
class Category (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var name: String,
    var description: String,
    var auditable: Boolean,

    @Column(insertable = false, updatable = false)
    val createdAt: String?
) {
    fun copy(id: Long? = this.id, name: String = this.name, description: String = this.description, auditable: Boolean = this.auditable, createdAt: String? = this.createdAt) =
        Category(id, name, description, auditable, createdAt)
}