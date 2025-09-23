package br.ufpr.tads.daily_iu_services.adapter.input.content

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/content")
class ContentController {

    @PostMapping
    fun createContent(): String {
        return "Content created"
    }

    @GetMapping
    fun getContent(): String {
        return "Content retrieved"
    }

    @GetMapping("/{id}")
    fun getContentById(): String {
        return "Content by ID retrieved"
    }

    @PutMapping("/{id}")
    fun updateContent(): String {
        return "Content updated"
    }

    @DeleteMapping("/{id}")
    fun deleteContent(): String {
        return "Content deleted"
    }
}