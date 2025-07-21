package com.pucetec.brito_andres_shipflow.models.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "shipment_history")
data class ShipmentHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    val shipment: Shipment,

    @Enumerated(EnumType.STRING)
    val status: ShipmentStatus,

    val occurredAt: LocalDateTime = LocalDateTime.now(),
    val comment: String? = null
)