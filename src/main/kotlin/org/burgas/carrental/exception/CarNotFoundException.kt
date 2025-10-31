package org.burgas.carrental.exception

import java.lang.RuntimeException

class CarNotFoundException(message: String) : RuntimeException(message)