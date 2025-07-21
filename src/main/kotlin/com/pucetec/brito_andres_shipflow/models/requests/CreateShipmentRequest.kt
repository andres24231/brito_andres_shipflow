package com.pucetec.brito_andres_shipflow.models.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Suppress("unused")
data class CreateShipmentRequest(
    @field:NotBlank(message = "Package type is required.")
    val type: String,
    @field:NotBlank(message = "Description is required.")
    @field:Size(max = 50, message = "Description cannot exceed 50 characters.")
    val description: String,
    val weight: Double,
    @field:NotBlank(message = "Origin city is required.")
    val originCity: String,
    @field:NotBlank(message = "Destination city is required.")
    val destinationCity: String
) 