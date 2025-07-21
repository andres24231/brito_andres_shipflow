package com.pucetec.brito_andres_shipflow.repositories

import com.pucetec.brito_andres_shipflow.models.entities.ShipmentHistory
import org.springframework.data.jpa.repository.JpaRepository

interface ShipmentHistoryRepository : JpaRepository<ShipmentHistory, Long>