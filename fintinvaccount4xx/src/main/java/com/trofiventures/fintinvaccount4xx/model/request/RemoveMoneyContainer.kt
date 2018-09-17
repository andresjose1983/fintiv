package com.trofiventures.fintinvaccount4xx.model.request

import java.io.Serializable

class RemoveMoneyContainer(var containerId: Int,
                           var contextRequest: ContextRequest,
                           var personId: String): Serializable