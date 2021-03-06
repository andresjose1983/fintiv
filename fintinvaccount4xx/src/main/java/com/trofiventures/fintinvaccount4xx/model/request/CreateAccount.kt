package com.trofiventures.fintinvaccount4xx.model.request

import com.trofiventures.fintinvaccount4xx.model.Attribute
import java.io.Serializable

class CreateAccount(var attributes: ArrayList<Attribute>,
                    var containerType: String = "SVA",
                    var contextRequest: ContextRequest,
                    var currency: String) : Serializable