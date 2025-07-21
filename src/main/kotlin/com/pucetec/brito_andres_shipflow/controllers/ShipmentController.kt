package com.pucetec.brito_andres_shipflow.controllers

import com.pucetec.brito_andres_shipflow.models.requests.CreateShipmentRequest
import com.pucetec.brito_andres_shipflow.models.requests.UpdateStatusRequest
import com.pucetec.brito_andres_shipflow.models.responses.ShipmentResponse
import com.pucetec.brito_andres_shipflow.services.ShipmentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@Suppress("unused")
@RestController
@RequestMapping("/shipments")
class ShipmentController(
    private val shipmentService: ShipmentService
) {

    @PostMapping
    fun createShipment(@RequestBody request: CreateShipmentRequest): ResponseEntity<ShipmentResponse> {
        val response = shipmentService.createShipment(request)
        return ResponseEntity.created(URI("/api/shipments/${response.id}")).body(response)
    }

    @GetMapping("/{trackingId}")
    fun getShipmentByTrackingId(@PathVariable trackingId: String): ResponseEntity<ShipmentResponse> {
        val response = shipmentService.getShipmentByTrackingId(trackingId)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getAllShipments(): ResponseEntity<List<ShipmentResponse>> {
        val response = shipmentService.getAllShipments()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{trackingId}/history")
    fun getShipmentHistory(@PathVariable trackingId: String): ResponseEntity<List<com.pucetec.brito_andres_shipflow.models.responses.ShipmentHistoryResponse>> {
        val response = shipmentService.getShipmentHistory(trackingId)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{id}/status")
    fun updateShipmentStatus(
        @PathVariable id: Long,
        @RequestBody request: UpdateStatusRequest
    ): ResponseEntity<ShipmentResponse> {
        val response = shipmentService.updateShipmentStatus(id, request)
        return ResponseEntity.ok(response)
    }
}