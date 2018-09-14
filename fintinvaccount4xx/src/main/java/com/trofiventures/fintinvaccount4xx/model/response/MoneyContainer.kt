package com.trofiventures.fintinvaccount4xx.model.response

import com.trofiventures.fintinvaccount4xx.model.Attribute
import java.io.Serializable

class MoneyContainer(var mozidoId: Int,
                     var containerId: Int,
                     var type: String,
                     var last4AccountDigits: String,
                     var subType: String?,
                     var description: String?,
                     var attributes: List<Attribute>): Serializable