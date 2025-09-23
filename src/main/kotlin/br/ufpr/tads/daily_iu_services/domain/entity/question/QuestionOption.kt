package br.ufpr.tads.daily_iu_services.domain.entity.question

import jakarta.persistence.*

@Entity
@Table(name = "questionOption")
class QuestionOption(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionId")
    val question: Question?,
    val label: String,

    @Column(name = "textValue")
    val value: String
)