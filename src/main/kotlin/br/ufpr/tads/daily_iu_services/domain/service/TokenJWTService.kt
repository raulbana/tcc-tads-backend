package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import br.ufpr.tads.daily_iu_services.domain.entity.user.UserPermissionEnum
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class TokenJWTService {

    @Value("\${project.dailyiu.security.token.secret}")
    lateinit var secret: String

    fun generateToken(user: User): String {
        try {
            val algoritmo = Algorithm.HMAC256(secret)
            return JWT.create()
                .withIssuer("br.ufpr.tads.daily_iu")
                .withSubject(user.id.toString())
                .withClaim("profile", UserPermissionEnum.fromLevel(user.role.permissionLevel).label)
                .withExpiresAt(expireDate())
                .sign(algoritmo)
        } catch (exception: JWTCreationException) {
            throw RuntimeException("erro ao gerar token jwt", exception)
        }
    }

    private fun expireDate(): Instant = LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"))
}