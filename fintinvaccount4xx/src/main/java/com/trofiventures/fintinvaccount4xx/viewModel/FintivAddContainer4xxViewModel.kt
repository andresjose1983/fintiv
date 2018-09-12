package com.trofiventures.fintinvaccount4xx.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.braintreepayments.cardform.view.CardForm
import com.cardconnect.consumersdk.CCConsumerTokenCallback
import com.cardconnect.consumersdk.domain.CCConsumerAccount
import com.cardconnect.consumersdk.domain.CCConsumerCardInfo
import com.cardconnect.consumersdk.domain.CCConsumerError
import com.cardconnect.consumersdk.enums.CCConsumerExpirationDateSeparator
import com.trofiventures.fintinvaccount4xx.FintivAccounts4xx
import com.trofiventures.fintinvaccount4xx.model.response.CreateAccountResponse
import com.trofiventures.fintivaccounts.services.RetrofitClient4xx
import org.jetbrains.anko.doAsync

class FintivAddContainer4xxViewModel(private val context: Application) : AndroidViewModel(context) {

    private val retrofit = RetrofitClient4xx()
    var error: MutableLiveData<String> = MutableLiveData()
    var createAccountResponse: MutableLiveData<CreateAccountResponse> = MutableLiveData()

    fun addCreditCard(accountNumber: String,
                      expireYear: String,
                      expireMonth: String,
                      accountName: String,
                      cvv: String,
                      description: String) {

        if (accountNumber.isEmpty()) {
            error.value = "error_account_number"
            return
        }

        if (expireYear.isEmpty()) {
            error.value = "error_expire_year"
            return
        }

        if (expireMonth.isEmpty()) {
            error.value = "error_expire_month"
            return
        }

        if (accountName.isEmpty()) {
            error.value = "error_account_name"
            return
        }

        if (cvv.isEmpty()) {
            error.value = "error_cvv"
            return
        }

        if (description.isEmpty()) {
            error.value = "error_description"
            return
        }

        validateCreditCardConnect(accountNumber, expireMonth, expireYear, cvv)

        /*doAsync {
            val containerSubType = if (accountNumber.startsWith("51") || accountNumber.startsWith("55") && accountNumber.length == 16) "MASTERCARD"
            else if (accountNumber.startsWith("4") && (accountNumber.length == 16 || accountNumber.length == 13)) "VISA"
            else if (accountNumber.startsWith("34") || accountNumber.startsWith("37") && accountNumber.length == 15) "AMERICANEXPRESS"
            else if ((accountNumber.startsWith("36") && accountNumber.length == 14) || (accountNumber.startsWith("55") && accountNumber.length == 16)) "DINERS"
            else if ((accountNumber.startsWith("6011") || accountNumber.startsWith("650")) || accountNumber.length == 16) "DISCOVER"
            else if ((accountNumber.startsWith("2131") || accountNumber.startsWith("1800")) || accountNumber.length == 15) "DISCOVER"
            else ""

            val moneyContainer = MoneyContainer(accountNumber, arrayListOf(Attribute("EXPIRE_YEAR", expireYear), Attribute("EXPIRE_MONTH", expireMonth), Attribute("ACCOUNT_NAME", accountName)),
                    description = description,
                    contextRequest = ContextRequest(Secrets4xx.TENANT, FintivAccounts4xx.currentToken(context).contextResponse.token),
                    attributesSecure = arrayListOf(Attribute("CREDIT_CARD_CVV", cvv)),
                    containerSubType = containerSubType)
            val call = retrofit.get()?.addMoneyContainer(moneyContainer)?.execute()
            val body = call?.body()
            if (body != null) {
                Log.d("hola", body.id.toString())
                createAccountResponse.postValue(body)
            }
        }*/
    }

    private fun validateCreditCardConnect(accountNumber: String,
                                          expireMonth: String,
                                          expireYear: String,
                                          cvv: String) {
        val cc = CCConsumerCardInfo()
        cc.cardNumber = accountNumber
        cc.expirationDate = expireMonth.plus("/").plus(expireYear)
        cc.ccConsumerExpirationDateSeparator = CCConsumerExpirationDateSeparator.SLASH
        cc.cvv = cvv

        if (!cc.isCardValid) {
            error.value = "error_invalid_cc"
            return
        }

        doAsync {

            FintivAccounts4xx.cardConnect()?.generateAccountForCard(cc, object : CCConsumerTokenCallback {
                override fun onCCConsumerTokenResponseError(p0: CCConsumerError) {
                    error.postValue(p0.responseMessage)
                }

                override fun onCCConsumerTokenResponse(p0: CCConsumerAccount) {
                    Log.d("hola", "${p0.token}")
                }
            })
        }
    }

    fun addCreditCard(cardForm: CardForm) {

        if (!cardForm.isValid) {
            cardForm.validate()
            return
        }

        validateCreditCardConnect(cardForm.cardNumber, cardForm.expirationMonth, cardForm.expirationYear, cardForm.cvv)
    }
}