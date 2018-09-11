package com.trofiventures.fintivaccounts.model

import java.io.Serializable

class FintivPerson(var personType: String = "SUBSCRIBER",
                   var firstName: String,
                   var lastName: String,
                   var password: String,
                   var timezoneFormatted: String = "EST"): Serializable