package com.trofiventures.fintivaccounts3xx.model.response

import java.io.Serializable
import java.util.*

class Fintinv3xxPerson(var id: String,
                       var tenantName: String,
                       var personType: String,
                       var firstName: String,
                       var lastName: String,
                       var timezone: String,
                       var timezoneOffset: String,
                       var status: String,
                       var statusChangeDatetime: Date,
                       var isAccountLocked: Boolean,
                       var lastLoginTimestamp: Date,
                       var activationDatetime: Date,
                       var createdDate: Date): Serializable