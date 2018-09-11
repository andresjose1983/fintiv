package com.trofiventures.fintivaccounts.model

import java.io.Serializable

class FintivPerson(var id: Int,
                   var username: String,
                   var password: String,
                   var personId: String,
                   var firstName: String,
                   var lastName: String,
                   var walletId: String,
                   var enabled: Boolean,
                   var accountNonExpired: Boolean,
                   var accountNonLocked: Boolean,
                   var credentialsNonExpired: Boolean): Serializable