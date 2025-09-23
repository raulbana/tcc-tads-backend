package br.ufpr.tads.daily_iu_services.adapter.input.preferences.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccessibilityDTO(
    val isBigFont: Boolean,
    val isHighContrast: Boolean
)