package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.contact.dto.ContactDTO
import br.ufpr.tads.daily_iu_services.adapter.input.contact.dto.ProfessionalRequestDTO
import br.ufpr.tads.daily_iu_services.adapter.output.user.MailClient
import br.ufpr.tads.daily_iu_services.adapter.output.user.UserRepository
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class ContactService(
    private val mailClient: MailClient,
    private val userRepository: UserRepository
) {

    fun sendEmail(email: ContactDTO) {
        mailClient.sendContactEmail(email)
    }

    fun sendProfessionalRequestEmail(request: ProfessionalRequestDTO) {
        val user = userRepository.findByEmailAndBlockedFalse(request.email) ?: throw NotFoundException("Usuário com email ${request.email} não encontrado.")

        if (user.blocked) {
            throw IllegalStateException("Usuário com email ${request.email} está bloqueado. Não é possível enviar solicitações profissionais.")
        }

        mailClient.sendProfessionalRequestEmail(user, request.reason)
    }
}