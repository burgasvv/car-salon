package org.burgas.carrental.router.contract

import org.burgas.carrental.service.contract.BaseService

interface Router<T : BaseService> {

    val service: T
}