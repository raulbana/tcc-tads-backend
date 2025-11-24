package br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AccessibilityDTO(
    @field:JsonProperty(required = true)
    val isBigFont: Boolean,

    @field:JsonProperty(required = true)
    val isHighContrast: Boolean,

    @field:JsonProperty(required = true)
    val isDarkMode: Boolean
)