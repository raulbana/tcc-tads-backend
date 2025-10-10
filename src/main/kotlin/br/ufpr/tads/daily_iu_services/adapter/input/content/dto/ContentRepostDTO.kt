package br.ufpr.tads.daily_iu_services.adapter.input.content.dto

import org.jetbrains.annotations.NotNull

data class ContentRepostDTO(
    @field:NotNull("O ID do conteúdo que está sendo repostado não pode ser nulo")
    val repostedFromContentId: Long,

    @field:NotNull("O ID do usuário que repostou não pode ser nulo")
    val repostedByUserId: Long
)