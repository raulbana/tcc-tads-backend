package br.ufpr.tads.daily_iu_services.adapter.input
import br.ufpr.tads.daily_iu_services.adapter.input.dto.AccessibilityDTO
import br.ufpr.tads.daily_iu_services.domain.service.AccessibilityService
import jakarta.validation.constraints.Min
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/preferences/accessibility")
class AccessibilityController(private val service: AccessibilityService) {
    @GetMapping
    fun getPreferences(@RequestHeader("x-user-id") @Min(1) userId: Long): ResponseEntity<AccessibilityDTO> {
        return ResponseEntity.ok(service.getPreferences(userId))
    }

    @PatchMapping
    fun setPreferences(
        @RequestHeader("x-user-id") @Min(1) userId: Long,
        @RequestBody() preferences: AccessibilityDTO
    ): ResponseEntity<Void> {
        service.setPreferences(userId, preferences)
        return ResponseEntity.noContent().build()
    }
}