package br.ufpr.tads.daily_iu_services.domain.entity.user

import jakarta.persistence.*

@Entity
@Table(name = "appUser")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var name: String,
    var email: String,
    var profilePictureUrl: String?,

    @OneToOne
    @JoinColumn(name = "patientProfileId", referencedColumnName = "id")
    val profile: PatientProfile,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "credentialId", referencedColumnName = "id")
    var credential: Credential,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "preferencesId", referencedColumnName = "id")
    val preferences: Preferences,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "roleId", referencedColumnName = "id")
    var role: Role,

    var strikes: Int = 0,
    var blocked: Boolean = false
)