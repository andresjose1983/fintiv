package com.trofiventures.fintinvaccount4xx.model.request

import java.io.Serializable

class Signon(var contextrequest: ContextRequest,
             var login: Login,
             var pass: Pass) : Serializable