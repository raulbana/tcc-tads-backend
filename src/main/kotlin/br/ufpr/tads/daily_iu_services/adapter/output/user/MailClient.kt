package br.ufpr.tads.daily_iu_services.adapter.output.user

import br.ufpr.tads.daily_iu_services.adapter.input.contact.dto.ContactDTO
import br.ufpr.tads.daily_iu_services.domain.entity.user.OTP
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MailClient(private val mailSender: JavaMailSender) {

    @Value("\${project.dailyiu.support.email}")
    private lateinit var supportEmail: String

    @Value("\${project.dailyiu.support.noreply}")
    private lateinit var noreplyEmail: String

    fun sendContactEmail(email: ContactDTO) {
        val message = SimpleMailMessage()
        message.from = noreplyEmail
        message.setTo(supportEmail)
        message.replyTo = email.userEmail
        message.setCc(email.userEmail)
        message.subject = email.subject
        message.text = email.text

        mailSender.send(message)
    }

    fun sendOtpEmail(to: String, otp: OTP) {
        val message = SimpleMailMessage()
        message.from = noreplyEmail
        message.setTo(to)
        message.subject = "Daily IU - Código OTP"
        message.text = "Seu código OTP é: ${otp.auxCode}. Ele é válido por 5 minutos."

        mailSender.send(message)
    }
}