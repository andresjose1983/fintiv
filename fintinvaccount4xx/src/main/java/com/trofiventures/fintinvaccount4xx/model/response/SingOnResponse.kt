package com.trofiventures.fintinvaccount4xx.model.response

import java.io.Serializable

class SingOnResponse(var contextResponse: ContextResponse,
                     var expirationTime: Long,
                     var person: Person?) : Serializable