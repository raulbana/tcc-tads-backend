package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.dto.AccessibilityDTO
import br.ufpr.tads.daily_iu_services.adapter.output.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class AccessibilityService(private val repository: UserRepository) {
    fun getPreferences(userId: Long): AccessibilityDTO {
        val user = repository.findById(userId)
            .orElseThrow { RuntimeException("Usuário não encontrado") }
        return AccessibilityDTO(user.isBigFont, user.isHighContrast)
    }

    @Transactional
    fun setPreferences(userId: Long, preferences: AccessibilityDTO) {
        val user = repository.findById(userId)
            .orElseThrow { RuntimeException("Usuário não encontrado") }
        user.isBigFont = preferences.isBigFont
        user.isHighContrast = preferences.isHighContrast
    }
}