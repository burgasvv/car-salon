package org.burgas.carrental.router

import org.burgas.carrental.service.BaseService

interface Router<T : BaseService> {

    val service: T
}