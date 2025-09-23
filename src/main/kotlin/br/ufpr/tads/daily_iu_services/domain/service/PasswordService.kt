package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.domain.entity.user.Credential
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*

@Service
class PasswordService {

    fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }

    @Throws(NoSuchAlgorithmException::class)
    fun hashPassword(password: String, salt: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(salt.toByteArray())
        val hashedPassword = md.digest(password.toByteArray())
        return Base64.getEncoder().encodeToString(hashedPassword)
    }

    @Throws(NoSuchAlgorithmException::class)
    fun verifyPassword(password: String, credential: Credential): Boolean {
        val hashedPassword = hashPassword(password, credential.salt)
        return hashedPassword == credential.passwordHash
    }
}