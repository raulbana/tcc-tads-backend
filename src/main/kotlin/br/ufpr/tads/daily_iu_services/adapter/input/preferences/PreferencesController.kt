package br.ufpr.tads.daily_iu_services.adapter.input.preferences

import br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto.AccessibilityDTO
import br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto.NotificationsDTO
import br.ufpr.tads.daily_iu_services.domain.service.PreferencesService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/preferences")
@Tag(name = "Preferências", description = "Endpoints para gerenciar as preferências do usuário")
class PreferencesController(private val service: PreferencesService) {

    @GetMapping("/accessibility")
    @Operation(summary = "Obter Preferências de Acessibilidade", description = "Recupera as preferências de acessibilidade de um usuário")
    fun getAccessibilityPreferences(@RequestHeader("x-user-id") userId: Long): ResponseEntity<AccessibilityDTO> {
        return ResponseEntity.ok(service.getAccessibilityPreferences(userId))
    }

    @PatchMapping("/accessibility")
    @Operation(summary = "Definir Preferências de Acessibilidade", description = "Atualiza as preferências de acessibilidade de um usuário")
    fun setAccessibilityPreferences(
        @RequestHeader("x-user-id") userId: Long,
        @RequestBody preferences: AccessibilityDTO
    ): ResponseEntity<Void> {
        service.setAccessibilityPreferences(userId, preferences)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/notifications")
    @Operation(summary = "Obter Preferências de Notificação", description = "Recupera as preferências de notificação de um usuário")
    fun getNotificationPreferences(@RequestHeader("x-user-id") userId: Long): ResponseEntity<NotificationsDTO> {
        return ResponseEntity.ok(service.getNotificationPreferences(userId))
    }

    @PatchMapping("/notifications")
    @Operation(summary = "Definir Preferências de Notificação", description = "Atualiza as preferências de notificação de um usuário")
    fun setNotificationPreferences(
        @RequestHeader("x-user-id") userId: Long,
        @RequestBody preferences: NotificationsDTO
    ): ResponseEntity<Void> {
        service.setNotificationPreferences(userId, preferences)
        return ResponseEntity.noContent().build()
    }
}