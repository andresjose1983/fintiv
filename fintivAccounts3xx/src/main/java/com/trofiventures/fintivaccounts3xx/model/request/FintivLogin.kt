package com.trofiventures.fintivaccounts3xx.model.request

import java.io.Serializable

class FintivLogin(var tenantName: String,
                  var credentialType: String = "EMAIL",
                  var securityElementType: String  = "password",
                  var login: String,
                  var pass: String): Serializable