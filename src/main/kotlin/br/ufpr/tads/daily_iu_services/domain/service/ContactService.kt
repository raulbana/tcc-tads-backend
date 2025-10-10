package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.contact.dto.ContactDTO
import br.ufpr.tads.daily_iu_services.adapter.output.user.MailClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class ContactService(private val mailClient: MailClient) {

    fun sendEmail(email: ContactDTO) {
        mailClient.sendContactEmail(email)
    }
}