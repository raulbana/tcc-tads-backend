package br.ufpr.tads.daily_iu_services.adapter.output.user

import br.ufpr.tads.daily_iu_services.adapter.input.contact.dto.ContactDTO
import br.ufpr.tads.daily_iu_services.domain.entity.content.Content
import br.ufpr.tads.daily_iu_services.domain.entity.user.OTP
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.time.LocalDate

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

    fun sendProfessionalRequestEmail(user: User, reason: String) {
        val message = SimpleMailMessage()
        val date = LocalDate.now()
        message.from = noreplyEmail
        message.setTo(supportEmail)
        message.replyTo = user.email
        message.setCc(user.email)
        message.subject = "Daily IU - Solicitação Profissional"
        message.text = "O usuário ${user.name} (e-mail: ${user.email}) solicitou uma conta profissional.\n\nMotivo:\n$reason\n\nData da solicitação: $date"

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

    fun sendContentShutdownWarning(content: Content, reportCount: Long) {
        val message = SimpleMailMessage()
        message.from = noreplyEmail
        message.setTo(content.author.email)
        message.setCc(supportEmail)
        message.subject = "Daily IU - Notificação de remoção de conteúdo"
        message.text = "Sua publicação com título \"${content.title}\" recebeu $reportCount denúncias e está sob revisão. Durante esse período, a publicação será ocultada da plataforma. Por favor, revise nossas diretrizes comunitárias para evitar futuras remoções."

        mailSender.send(message)
    }

    fun sendUserBanWarning(userEmail: String, contentTitle: String, strikeCount: Int) {
        val message = SimpleMailMessage()
        message.from = noreplyEmail
        message.setTo(userEmail)
        message.setCc(supportEmail)
        message.subject = "Daily IU - Notificação de advertência"
        message.text = "Sua conta recebeu $strikeCount advertência(s) devido a violações das diretrizes da comunidade pela publicação \"$contentTitle\", que será permanentemente removida da plataforma.\n\nPor favor, revise nossas diretrizes comunitárias para evitar futuras advertências. A reincidência de advertências poderá levar a uma suspensão temporária.\n\nSe você acredita que isso foi um erro, entre em contato com nossa equipe de suporte."

        mailSender.send(message)
    }

    fun sendUserBanNotification(userEmail: String) {
        val message = SimpleMailMessage()
        message.from = noreplyEmail
        message.setTo(userEmail)
        message.setCc(supportEmail)
        message.subject = "Daily IU - Notificação de suspensão de usuário"
        message.text = "Sua conta foi suspensa temporariamente devido a múltiplas violações das diretrizes da comunidade.\n\nSe você acredita que isso foi um erro, entre em contato com nossa equipe de suporte."

        mailSender.send(message)
    }
}