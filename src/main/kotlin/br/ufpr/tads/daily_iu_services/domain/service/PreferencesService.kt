package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto.AccessibilityDTO
import br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto.mapper.AccessibilityMapper
import br.ufpr.tads.daily_iu_services.adapter.output.user.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PreferencesService(private val repository: UserRepository) {

    fun getAccessibilityPreferences(userId: Long): AccessibilityDTO {
        val user = repository.findById(userId)
            .orElseThrow { RuntimeException("Usuário não encontrado") }
        return AccessibilityMapper.INSTANCE.UserToAccessibilityDTO(user)
    }

    @Transactional
    fun setAccessibilityPreferences(userId: Long, preferences: AccessibilityDTO) {
        val user = repository.findById(userId)
            .orElseThrow { RuntimeException("Usuário não encontrado") }
        user.preferences.bigFont = preferences.isBigFont
        user.preferences.highContrast = preferences.isHighContrast

        repository.save(user)
    }
}