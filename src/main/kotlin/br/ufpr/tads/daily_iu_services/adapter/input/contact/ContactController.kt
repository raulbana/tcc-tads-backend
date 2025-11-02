package br.ufpr.tads.daily_iu_services.adapter.input.contact

import br.ufpr.tads.daily_iu_services.adapter.input.contact.dto.ContactDTO
import br.ufpr.tads.daily_iu_services.adapter.input.contact.dto.ProfessionalRequestDTO
import br.ufpr.tads.daily_iu_services.domain.service.ContactService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/contact")
@Tag(name = "Contato", description = "Endpoints relacionados ao contato e suporte")
class ContactController(private val service: ContactService) {
    @PostMapping("/support")
    @Operation(summary = "Enviar e-mail de contato", description = "Envia um e-mail usando o formulário de contato")
    fun sendEmail(@RequestBody email: ContactDTO): ResponseEntity<Void> {
        service.sendEmail(email)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/professional-request")
    @Operation(summary = "Enviar e-mail de solicitação profissional", description = "Envia um e-mail de solicitação profissional usando o formulário de contato")
    @ApiResponse(responseCode = "204", description = "E-mail enviado com sucesso")
    @ApiResponse(responseCode = "409", description = "Usuário bloqueado não pode enviar solicitações profissionais")
    fun sendProfessionalRequestEmail(@RequestBody @Valid request: ProfessionalRequestDTO): ResponseEntity<Void> {
        service.sendProfessionalRequestEmail(request)
        return ResponseEntity.noContent().build()
    }
}