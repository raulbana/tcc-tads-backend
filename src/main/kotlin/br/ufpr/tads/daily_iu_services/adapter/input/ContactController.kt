package br.ufpr.tads.daily_iu_services.adapter.input

import br.ufpr.tads.daily_iu_services.adapter.input.dto.ContactDTO
import br.ufpr.tads.daily_iu_services.domain.service.ContactService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/contact")
class ContactController(private val service: ContactService) {
    @PostMapping
    fun sendEmail(@RequestBody email: ContactDTO): ResponseEntity<Void> {
        service.sendEmail(email)
        return ResponseEntity.noContent().build()
    }
}