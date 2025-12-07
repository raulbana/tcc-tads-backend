package br.ufpr.tads.daily_iu_services.domain.service

import br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto.AccessibilityDTO
import br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto.NotificationsDTO
import br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto.mapper.PreferencesMapper
import br.ufpr.tads.daily_iu_services.adapter.output.user.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PreferencesService(private val repository: UserRepository) {

    fun getAccessibilityPreferences(userId: Long): AccessibilityDTO {
        val user = repository.findById(userId)
            .orElseThrow { RuntimeException("Usuário não encontrado") }
        return PreferencesMapper.INSTANCE.userToAccessibilityDTO(user)
    }

    @Transactional
    fun setAccessibilityPreferences(userId: Long, preferences: AccessibilityDTO) {
        val user = repository.findById(userId)
            .orElseThrow { RuntimeException("Usuário não encontrado") }
        user.preferences.bigFont = preferences.isBigFont
        user.preferences.highContrast = preferences.isHighContrast
        user.preferences.darkMode = preferences.isDarkMode

        repository.save(user)
    }

    fun getNotificationPreferences(userId: Long): NotificationsDTO {
        val user = repository.findById(userId)
            .orElseThrow { RuntimeException("Usuário não encontrado") }
        return PreferencesMapper.INSTANCE.userToNotificationsDTO(user)
    }

    @Transactional
    fun setNotificationPreferences(userId: Long, preferences: NotificationsDTO) {
        val user = repository.findById(userId)
            .orElseThrow { RuntimeException("Usuário não encontrado") }
        user.preferences.reminderCalendar = preferences.reminderCalendar
        user.preferences.reminderCalendarSchedule = preferences.reminderCalendarSchedule
        user.preferences.reminderWorkout = preferences.reminderWorkout
        user.preferences.reminderWorkoutSchedule = preferences.reminderWorkoutSchedule
        user.preferences.encouragingMessages = preferences.encouragingMessages
        user.preferences.notificationToken = preferences.notificationToken

        repository.save(user)
    }
}