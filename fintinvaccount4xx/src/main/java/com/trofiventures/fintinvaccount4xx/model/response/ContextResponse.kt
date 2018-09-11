package com.trofiventures.fintinvaccount4xx.model.response

import java.io.Serializable

class ContextResponse(var token: String,
                      var tenantName: String,
                      var statusCode: String) : Serializable