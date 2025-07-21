package com.pucetec.brito_andres_shipflow.repositories

import com.pucetec.brito_andres_shipflow.models.entities.Shipment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ShipmentRepository : JpaRepository<Shipment, Long> {
    fun findByTrackingId(trackingId: String): Optional<Shipment>
}