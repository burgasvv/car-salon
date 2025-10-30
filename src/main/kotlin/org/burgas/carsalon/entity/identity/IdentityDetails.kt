package org.burgas.carsalon.entity.identity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class IdentityDetails : UserDetails {

    val identity: Identity

    constructor(identity: Identity) {
        this.identity = identity
    }

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return mutableListOf(this.identity.authority)
    }

    override fun getPassword(): String {
        return identity.password
    }

    override fun getUsername(): String {
        return this.identity.email
    }

    override fun isEnabled(): Boolean {
        return this.identity.enabled || !super.isEnabled()
    }
}