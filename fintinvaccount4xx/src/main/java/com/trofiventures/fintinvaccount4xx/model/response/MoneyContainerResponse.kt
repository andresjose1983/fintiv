package com.trofiventures.fintinvaccount4xx.model.response

import java.io.Serializable

class MoneyContainerResponse(var contextResponse: ContextResponse,
                             var moneyContainers: List<MoneyContainer>): Serializable