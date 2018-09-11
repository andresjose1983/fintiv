package com.trofiventures.fintivaccounts.model

import java.io.Serializable

class FintivToken(var access_token: String,
                  var token_type: String,
                  var refresh_token: String,
                  var expires_in: Long,
                  var scope: String,
                  var jti: String,
                  var fintivPerson: FintivPerson?) : Serializable