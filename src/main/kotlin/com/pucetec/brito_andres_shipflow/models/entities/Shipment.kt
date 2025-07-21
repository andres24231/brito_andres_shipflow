package com.pucetec.brito_andres_shipflow.models.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "shipments")
data class Shipment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val trackingId: String = "",
    @Enumerated(EnumType.STRING)
    var status: ShipmentStatus = ShipmentStatus.PENDING,
    val type: String,
    val description: String,
    val weight: Double,
    val originCity: String,
    val destinationCity: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val estimatedDeliveryDate: LocalDateTime = LocalDateTime.now().plusDays(5),

    @OneToMany(mappedBy = "shipment", cascade = [CascadeType.ALL], orphanRemoval = true)
    val history: MutableList<ShipmentHistory> = mutableListOf()
)