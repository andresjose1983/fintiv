package com.trofiventures.fintivaccounts3xx.model.response

import java.io.Serializable

class SignonInfo(var contextResponse: SignonContextResponse,
                 var resetRequired: Boolean,
                 var expirationTime: Long) :Serializable