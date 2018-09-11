package com.trofiventures.fintivaccounts3xx.model.response

import java.io.Serializable

class SignonContextResponse(var token: String,
                            var tenantName: String,
                            var statusCode: String): Serializable