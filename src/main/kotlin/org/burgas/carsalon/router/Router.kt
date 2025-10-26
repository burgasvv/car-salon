package org.burgas.carsalon.router

import org.burgas.carsalon.service.BaseService

interface Router<T : BaseService> {

    val service: T
}