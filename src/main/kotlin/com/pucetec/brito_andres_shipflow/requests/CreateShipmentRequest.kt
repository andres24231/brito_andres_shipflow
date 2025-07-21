package com.pucetec.brito_andres_shipflow.requests

data class CreateShipmentRequest(
    val type: String,
    val description: String,
    val weight: Double,
    val originCity: String,
    val destinationCity: String
)