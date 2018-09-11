package com.trofiventures.fintinvaccount4xx.model.request

import java.io.Serializable

class MoneyContainer(var accountNumber: String,
                     var attributes: ArrayList<Attribute>,
                     var attributesSecure: ArrayList<Attribute>,
                     var containerSubType: String,
                     var containerType: String = "CC",
                     var contextRequest: ContextRequest,
                     var currency: String = "840",
                     var description: String): Serializable