package com.trofiventures.fintinvaccount4xx.model.request

import java.io.Serializable

class CreditCardCardConnect(var merchid: String,
                            var orderid: String,
                            var accttype: String,
                            var account: String,
                            var expiry: String,
                            var amount: String = "0",
                            var currency: String = "USD",
                            var cvv2: String,
                            var tokenize: String = "Y",
                            var profile: String = "Y"): Serializable