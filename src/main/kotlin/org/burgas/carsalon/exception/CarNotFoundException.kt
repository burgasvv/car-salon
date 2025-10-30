package org.burgas.carsalon.exception

import java.lang.RuntimeException

class CarNotFoundException(message: String) : RuntimeException(message)