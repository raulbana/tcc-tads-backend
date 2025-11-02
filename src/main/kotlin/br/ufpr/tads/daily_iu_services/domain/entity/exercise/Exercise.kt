package br.ufpr.tads.daily_iu_services.domain.entity.exercise

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "exercise")
data class Exercise(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var title: String,

    @ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    var category: ExerciseCategory,
    var instructions: String,
    var repetitions: Int,
    var sets: Int,
    var restTime: Int,
    var duration: Int,

    @OneToMany(mappedBy = "exercise", cascade = [CascadeType.ALL], orphanRemoval = true)
    val media: MutableList<ExerciseMedia> = mutableListOf(),

    @OneToMany(mappedBy = "exercise", cascade = [CascadeType.ALL], orphanRemoval = true)
    val attributes: MutableList<ExerciseAttributeExercise> = mutableListOf(),

    @Column(insertable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
