package com.pucetec.brito_andres_shipflow.exceptions

class ShipmentNotFoundException(id: String) :
    RuntimeException("No se encontró un envío con el tracking ID: $id")