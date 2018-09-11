package com.trofiventures.fintinvaccount4xx.model.request

import java.io.Serializable

class Pass(var securityElementType: String,
           var value: String) : Serializable