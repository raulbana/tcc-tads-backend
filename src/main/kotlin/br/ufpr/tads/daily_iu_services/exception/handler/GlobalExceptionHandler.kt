package br.ufpr.tads.daily_iu_services.exception.handler

import br.ufpr.tads.daily_iu_services.exception.NoContentException
import br.ufpr.tads.daily_iu_services.exception.NotAllowedException
import br.ufpr.tads.daily_iu_services.exception.NotFoundException
import br.ufpr.tads.daily_iu_services.exception.domain.ExceptionOrigin
import br.ufpr.tads.daily_iu_services.exception.domain.Message
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.MaxUploadSizeExceededException
import java.sql.SQLException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxSizeException(ex: MaxUploadSizeExceededException?): ResponseEntity<Message> {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body(
                Message(
                    HttpStatus.PAYLOAD_TOO_LARGE.value(),
                    ExceptionOrigin.REQUEST.origin,
                    "O arquivo enviado é muito grande"
                )
            )
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<Message> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                Message(
                    HttpStatus.NOT_FOUND.value(),
                    ExceptionOrigin.REQUEST.origin,
                    ex.message ?: "Recurso não encontrado"
                )
            )
    }

    @ExceptionHandler(NoContentException::class)
    fun handleNoContentException(ex: NoContentException): ResponseEntity<Void> {
        return ResponseEntity.noContent().build()
    }

    @ExceptionHandler(NotAllowedException::class)
    fun handleNotAllowedException(ex: NotAllowedException): ResponseEntity<Message> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(
                Message(
                    HttpStatus.FORBIDDEN.value(),
                    ExceptionOrigin.REQUEST.origin,
                    ex.message ?: "Ação não permitida"
                )
            )
    }

    @ExceptionHandler(NotImplementedError::class)
    fun handleNotImplementedError(): ResponseEntity<Message> {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
            .body(
                Message(
                    HttpStatus.NOT_IMPLEMENTED.value(),
                    ExceptionOrigin.INTERNAL.origin,
                    "Funcionalidade não implementada (ainda)!"
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Message> {
        val errors = ex.bindingResult.allErrors.map { error ->
            val fieldName = if (error is FieldError) {
                error.field
            } else {
                error.objectName
            }
            "$fieldName: ${error.defaultMessage}"
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                Message(
                    HttpStatus.BAD_REQUEST.value(),
                    ExceptionOrigin.REQUEST.origin,
                    "Erros de validação nos parâmetros de entrada",
                    errors
                )
            )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<Message> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                Message(
                    HttpStatus.BAD_REQUEST.value(),
                    ExceptionOrigin.REQUEST.origin,
                    "Erros de validação nos parâmetros de entrada",
                    listOf(ex.message ?: "Argumento ilegal fornecido")
                )
            )
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<Message> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                Message(
                    HttpStatus.CONFLICT.value(),
                    ExceptionOrigin.REQUEST.origin,
                    ex.message ?: "Estado ilegal"
                )
            )
    }

    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleMissingRequestHeaderException(ex: MissingRequestHeaderException): ResponseEntity<Message> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                Message(
                    HttpStatus.BAD_REQUEST.value(),
                    ExceptionOrigin.REQUEST.origin,
                    "Cabeçalho de requisição ausente: ${ex.headerName}"
                )
            )
    }

    @ExceptionHandler(SQLException::class)
    fun handleSQLException(ex: SQLException): ResponseEntity<Message> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                Message(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ExceptionOrigin.DATABASE.origin,
                    "Erro de banco de dados: ${ex.message}"
                )
            )
    }
}