package com.trofiventures.fintivaccounts3xx.model.request

import java.io.Serializable

class FintivPersonCredentials(var credentialType: String = "EMAIL",
                              var credential: String): Serializable