package com.pucetec.brito_andres_shipflow.exceptions

class OriginDestinationSameException :
    RuntimeException("La ciudad de origen no puede ser igual a la de destino.")