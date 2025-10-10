package br.ufpr.tads.daily_iu_services.adapter.input.preferences

import br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto.AccessibilityDTO
import br.ufpr.tads.daily_iu_services.domain.service.PreferencesService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Min
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/preferences")
@Tag(name = "Preferences", description = "Endpoints for managing user preferences")
class PreferencesController(private val service: PreferencesService) {

    @GetMapping("/accessibility")
    @Operation(summary = "Get Accessibility Preferences", description = "Retrieve the accessibility preferences for a user")
    fun getAccessibilityPreferences(@RequestHeader("x-user-id") @Min(1) userId: Long): ResponseEntity<AccessibilityDTO> {
        return ResponseEntity.ok(service.getAccessibilityPreferences(userId))
    }

    @PatchMapping("/accessibility")
    @Operation(summary = "Set Accessibility Preferences", description = "Update the accessibility preferences for a user")
    fun setAccessibilityPreferences(
        @RequestHeader("x-user-id") @Min(1) userId: Long,
        @RequestBody() preferences: AccessibilityDTO
    ): ResponseEntity<Void> {
        service.setAccessibilityPreferences(userId, preferences)
        return ResponseEntity.noContent().build()
    }
}