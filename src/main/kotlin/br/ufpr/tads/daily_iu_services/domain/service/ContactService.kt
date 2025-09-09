package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.dto.ContactDTO
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class ContactService(private val mailSender: JavaMailSender) {
    fun sendEmail(email: ContactDTO) {
        val message = SimpleMailMessage()
        message.setTo("suporte.daily.iu@gmail.com")
        message.replyTo = email.userEmail
        message.setCc(email.userEmail)
        message.subject = email.subject
        message.text = email.text

        mailSender.send(message)
    }
}