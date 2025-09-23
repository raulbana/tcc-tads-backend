package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.contact.dto.ContactDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class ContactService(private val mailSender: JavaMailSender) {

    @Value("\${project.dailyiu.support.email}")
    private lateinit var supportEmail: String

    @Value("\${project.dailyiu.support.noreply}")
    private lateinit var noreplyEmail: String

    fun sendEmail(email: ContactDTO) {
        val message = SimpleMailMessage()
        message.from = noreplyEmail
        message.setTo(supportEmail)
        message.replyTo = email.userEmail
        message.setCc(email.userEmail)
        message.subject = email.subject
        message.text = email.text

        mailSender.send(message)
    }
}