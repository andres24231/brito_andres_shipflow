package com.pucetec.brito_andres_shipflow.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.pucetec.brito_andres_shipflow.models.requests.CreateShipmentRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class ShipmentControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun getAllShipments_returnsOk() {
        mockMvc.perform(get("/shipments"))
            .andExpect(status().isOk)
    }

    @Test
    fun createShipment_returnsCreated() {
        val request = CreateShipmentRequest("type", "desc", 1.0, "A", "B")
        mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated)
    }
}
