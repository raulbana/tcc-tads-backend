package br.ufpr.tads.daily_iu_services.adapter.input.contact

import br.ufpr.tads.daily_iu_services.adapter.input.contact.dto.ContactDTO
import br.ufpr.tads.daily_iu_services.domain.service.ContactService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/contact")
@Tag(name = "Contact", description = "Endpoints for contact form submissions")
class ContactController(private val service: ContactService) {
    @PostMapping
    @Operation(summary = "Send contact email", description = "Send an email using the contact form")
    fun sendEmail(@RequestBody email: ContactDTO): ResponseEntity<Void> {
        service.sendEmail(email)
        return ResponseEntity.noContent().build()
    }
}