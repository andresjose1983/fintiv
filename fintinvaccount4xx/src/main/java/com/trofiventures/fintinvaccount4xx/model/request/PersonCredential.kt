package com.trofiventures.fintinvaccount4xx.model.request

import java.io.Serializable

class PersonCredential(var contextRequest: ContextRequest,
                       var credential: String,
                       var credentialType: String = "EMAIL",
                       var personid: String): Serializable