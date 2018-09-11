package com.trofiventures.fintinvaccount4xx.model.request

import java.io.Serializable

class CreatePerson(var contextRequest: ContextRequest,
                   var firstName: String,
                   var language: String = "EN",
                   var language_country: String = "US",
                   var lastName: String,
                   var password: String,
                   var personType: String = "SUBSCRIBER",
                   var pin: String = "1313",
                   var timezone: String = "ETC"): Serializable