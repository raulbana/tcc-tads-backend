package br.ufpr.tads.daily_iu_services.exception.domain

enum class ExceptionOrigin(val origin: String) {
    REQUEST("REQUEST"),
    DATABASE("DATABASE"),
    EXTERNAL_SERVICE("EXTERNAL_SERVICE"),
    INTERNAL("INTERNAL")
}