package com.trofiventures.fintivaccounts3xx.model.request

import com.trofiventures.fintivaccounts.model.FintivPerson
import java.io.Serializable

class FintivPersonRequest(var person: FintivPerson,
                          var personCredentials: ArrayList<FintivPersonCredentials>) : Serializable