package com.pucetec.brito_andres_shipflow.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@Suppress("unused")
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(InvalidStatusTransitionException::class)
    fun handleInvalidStatusTransition(ex: InvalidStatusTransitionException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to (ex.message ?: "Invalid status transition")))
    }

    @ExceptionHandler(OriginDestinationSameException::class)
    fun handleOriginDestinationSame(ex: OriginDestinationSameException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to (ex.message ?: "Origin and destination cannot be the same")))
    }

    @ExceptionHandler(ShipmentNotFoundException::class)
    fun handleShipmentNotFound(ex: ShipmentNotFoundException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to (ex.message ?: "Shipment not found")))
    }

    @ExceptionHandler(StatusValidationException::class)
    fun handleStatusValidation(ex: StatusValidationException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to (ex.message ?: "Status validation error")))
    }

    // Generic handler for any other uncaught exception
    @ExceptionHandler(Exception::class)
    fun handleGenericError(ex: Exception): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to ("An internal error occurred: ${ex.message}")))
    }
}