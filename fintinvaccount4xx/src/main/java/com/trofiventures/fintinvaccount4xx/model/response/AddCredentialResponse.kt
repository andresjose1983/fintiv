package com.trofiventures.fintinvaccount4xx.model.response

import java.io.Serializable

class AddCredentialResponse(var contextResponse: ContextResponse,
                            var credentialType: String,
                            var personId: String,
                            var credential: String,
                            var status: String) : Serializable