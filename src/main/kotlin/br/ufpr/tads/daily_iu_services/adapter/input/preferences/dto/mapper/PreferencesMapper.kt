package br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto.mapper

import br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto.AccessibilityDTO
import br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto.NotificationsDTO
import br.ufpr.tads.daily_iu_services.domain.entity.user.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
interface PreferencesMapper {

    companion object {
        val INSTANCE: PreferencesMapper = Mappers.getMapper(PreferencesMapper::class.java)
    }

    @Mapping(target = "isBigFont", source = "user.preferences.bigFont")
    @Mapping(target = "isHighContrast", source = "user.preferences.highContrast")
    @Mapping(target = "isDarkMode", source = "user.preferences.darkMode")
    fun userToAccessibilityDTO(user: User): AccessibilityDTO

    @Mapping(target = "reminderCalendar", source = "user.preferences.reminderCalendar")
    @Mapping(target = "reminderCalendarSchedule", source = "user.preferences.reminderCalendarSchedule")
    @Mapping(target = "reminderWorkout", source = "user.preferences.reminderWorkout")
    @Mapping(target = "reminderWorkoutSchedule", source = "user.preferences.reminderWorkoutSchedule")
    @Mapping(target = "encouragingMessages", source = "user.preferences.encouragingMessages")
    @Mapping(target = "notificationToken", source = "user.preferences.notificationToken")
    fun userToNotificationsDTO(user: User): NotificationsDTO
}