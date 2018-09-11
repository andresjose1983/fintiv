package com.trofiventures.fintinvaccount4xx.model.request

import java.io.Serializable

class ContextRequest(var tenantName: String,
                     var token: String? = null): Serializable