package br.ufpr.tads.daily_iu_services.domain.entity.question

import jakarta.persistence.*

@Entity
@Table(name = "question")
class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val externalId: String,
    val text: String,

    @Enumerated(EnumType.STRING)
    val type: QuestionTypeEnum,

    @OneToMany(mappedBy = "question", targetEntity = QuestionOption::class, cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var options: List<QuestionOption>?,

    @Column(name = "minValue")
    val min: Int?,
    @Column(name = "maxValue")
    val max: Int?,
    val step: Int?,
    val required: Boolean?,
    val placeholder: String?
)