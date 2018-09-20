package com.trofiventures.fintinvaccount4xx.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Base64
import android.util.Log
import com.braintreepayments.cardform.view.CardForm
import com.cardconnect.consumersdk.domain.CCConsumerAccount
import com.cardconnect.consumersdk.domain.CCConsumerCardInfo
import com.cardconnect.consumersdk.enums.CCConsumerExpirationDateSeparator
import com.trofiventures.fintinvaccount4xx.FintivAccounts4xx
import com.trofiventures.fintinvaccount4xx.model.Attribute
import com.trofiventures.fintinvaccount4xx.model.request.*
import com.trofiventures.fintinvaccount4xx.model.response.CreateAccountResponse
import com.trofiventures.fintinvaccount4xx.model.response.CreditCardCardConnectToken
import com.trofiventures.fintivaccounts.services.RetrofitClient4xx
import com.trofiventures.fintivaccounts.services.RetrofitClientCardConnect
import com.trofiventures.fintivaccounts.util.Secrets4xx
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FintivMoneyContainer4xxViewModel(private val context: Application) : AndroidViewModel(context) {

    private val retrofit = RetrofitClient4xx()
    private val retrofitCardConnect = RetrofitClientCardConnect()
    var error: MutableLiveData<String> = MutableLiveData()
    var cardConnectToken: MutableLiveData<String> = MutableLiveData()
    var createAccountResponse: MutableLiveData<CreateAccountResponse> = MutableLiveData()
    var moneyContainers: MutableLiveData<List<com.trofiventures.fintinvaccount4xx.model.response.MoneyContainer>> = MutableLiveData()
    var moneyContainerRemoved: MutableLiveData<Int> = MutableLiveData()

    fun addCreditCard(accountNumber: String,
                      expireYear: String,
                      expireMonth: String,
                      accountName: String,
                      cvv: String,
                      description: String,
                      default: Boolean) {

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

        validateCreditCardConnect(accountNumber, expireMonth, expireYear, cvv, accountName, description, default)
    }

    private fun validateCreditCardConnect(accountNumber: String,
                                          expireMonth: String,
                                          expireYear: String,
                                          cvv: String,
                                          cardHolder: String,
                                          description: String, default: Boolean) {
        val cc = CCConsumerCardInfo()
        cc.cardNumber = accountNumber
        cc.expirationDate = expireMonth.plus("/").plus(expireYear)
        cc.ccConsumerExpirationDateSeparator = CCConsumerExpirationDateSeparator.SLASH
        cc.cvv = cvv

        if (!cc.isCardValid) {
            error.value = "error_invalid_cc"
            return
        }

        if (Secrets4xx.TENANT.isEmpty()) {
            error.value = "error_tenant"
            return
        }

        if (Secrets4xx.MERCHANT_ID.isEmpty()) {
            error.value = "error_merchant_id"
            return
        }

        if (Secrets4xx.USER.isEmpty()) {
            error.value = "error_user"
            return
        }

        if (Secrets4xx.PASSWORD.isEmpty()) {
            error.value = "error_password"
            return
        }

        val token = FintivAccounts4xx.currentToken(context)
        if (token == null) {
            error.value = "error_security"
            return
        }

        doAsync {

            val containerSubType = if (accountNumber.startsWith("51") || accountNumber.startsWith("55") && accountNumber.length == 16) "MASTERCARD"
            else if (accountNumber.startsWith("4") && (accountNumber.length == 16 || accountNumber.length == 13)) "VISA"
            else if (accountNumber.startsWith("34") || accountNumber.startsWith("37") && accountNumber.length == 15) "AMERICANEXPRESS"
            else if ((accountNumber.startsWith("36") && accountNumber.length == 14) || (accountNumber.startsWith("55") && accountNumber.length == 16)) "DINERS"
            else if ((accountNumber.startsWith("6011") || accountNumber.startsWith("650")) || accountNumber.length == 16) "DISCOVER"
            else if ((accountNumber.startsWith("2131") || accountNumber.startsWith("1800")) || accountNumber.length == 15) "DISCOVER"
            else ""

            val creditCardCardConnect = CreditCardCardConnect(merchid = Secrets4xx.MERCHANT_ID,
                    orderid = "",
                    accttype = containerSubType,
                    account = accountNumber,
                    expiry = expireMonth.plus(expireYear),
                    cvv2 = cvv)

            val basic =
                    "Basic " + Base64.encodeToString(Secrets4xx.USER.plus(":").plus(Secrets4xx.PASSWORD).toByteArray(), Base64.NO_WRAP)

            retrofitCardConnect.get()?.generateCreditCardToken(basic, creditCardCardConnect)?.enqueue(object : Callback<CreditCardCardConnectToken> {
                override fun onFailure(call: Call<CreditCardCardConnectToken>, t: Throwable) {
                    error.postValue(t.message)
                }

                override fun onResponse(call: Call<CreditCardCardConnectToken>, response: Response<CreditCardCardConnectToken>) {
                    val body = response.body()
                    if(body != null){
                        cardConnectToken.postValue(body.token)
                        sendCardToken(accountNumber, cvv, expireYear, expireMonth, cardHolder, description, body, default)
                    }
                }
            })
        }
    }

    private fun sendCardToken(accountNumber: String,
                              cvv: String,
                              expireYear: String,
                              expireMonth: String,
                              accountName: String,
                              description: String,
                              cc: CreditCardCardConnectToken,
                              default: Boolean) {

        if (Secrets4xx.TENANT.isEmpty()) {
            error.value = "error_tenant"
            return
        }

        val token = FintivAccounts4xx.currentToken(context)
        if (token == null) {
            error.value = "error_security"
            return
        }

        doAsync {
            val containerSubType = if (accountNumber.startsWith("51") || accountNumber.startsWith("55") && accountNumber.length == 16) "MASTERCARD"
            else if (accountNumber.startsWith("4") && (accountNumber.length == 16 || accountNumber.length == 13)) "VISA"
            else if (accountNumber.startsWith("34") || accountNumber.startsWith("37") && accountNumber.length == 15) "AMERICANEXPRESS"
            else if ((accountNumber.startsWith("36") && accountNumber.length == 14) || (accountNumber.startsWith("55") && accountNumber.length == 16)) "DINERS"
            else if ((accountNumber.startsWith("6011") || accountNumber.startsWith("650")) || accountNumber.length == 16) "DISCOVER"
            else if ((accountNumber.startsWith("2131") || accountNumber.startsWith("1800")) || accountNumber.length == 15) "DISCOVER"
            else ""

            val moneyContainer = MoneyContainer(accountNumber,
                    arrayListOf(
                            Attribute("EXPIRE_YEAR", expireYear),
                            Attribute("EXPIRE_MONTH", expireMonth),
                            Attribute("ACCOUNT_NAME", accountName),
                            Attribute("INDUSTRYSPECIFICDATA", cc.token),
                            Attribute("PERSONAL_ACCOUNT_NUMBER", cc.profileid),
                            Attribute("ACCOUNT", cc.account)
                    ),
                    description = description,
                    contextRequest = ContextRequest(Secrets4xx.TENANT, token.contextResponse.token),
                    attributesSecure = arrayListOf(Attribute("CREDIT_CARD_CVV", cvv)),
                    containerSubType = containerSubType,
                    isDefault = default)
            val call = retrofit.get()?.addMoneyContainer(moneyContainer)?.execute()
            val body = call?.body()
            if (body != null) {
                Log.d("hola", "Cardconnect token ${cc.token}" + body.id.toString())
                createAccountResponse.postValue(body)
            } else
                error.postValue(body?.contextResponse?.statusCode)
        }
    }

    fun addCreditCard(cardForm: CardForm, cardHolder: String,
                      description: String, default: Boolean = false) {

        if (!cardForm.isValid) {
            cardForm.validate()
            return
        }

        if (cardHolder.isEmpty()) {
            error.value = "error_account_name"
            return
        }

        if (description.isEmpty()) {
            error.value = "error_description"
            return
        }

        validateCreditCardConnect(cardForm.cardNumber, cardForm.expirationMonth,
                cardForm.expirationYear, cardForm.cvv, cardHolder, description, default)
    }

    fun getMoneyContainers() {

        if (Secrets4xx.TENANT.isEmpty()) {
            error.value = "error_tenant"
            return
        }

        val token = FintivAccounts4xx.currentToken(context)
        if (token == null) {
            error.value = "error_security"
            return
        }
        doAsync {
            val contextRequest = ContextRequest(Secrets4xx.TENANT, token.contextResponse.token)
            val call = retrofit.get()?.getMoneyContainers(SendToken(contextRequest))?.execute()
            val body = call?.body()
            if (body != null)
                moneyContainers.postValue(body.moneyContainers)
            else
                error.postValue(body?.contextResponse?.statusCode)
        }
    }

    fun delete(moneyContainer: com.trofiventures.fintinvaccount4xx.model.response.MoneyContainer) {
        if (Secrets4xx.TENANT.isEmpty()) {
            error.value = "error_tenant"
            return
        }

        val token = FintivAccounts4xx.currentToken(context)
        if (token == null) {
            error.value = "error_security"
            return
        }
        doAsync {
            val contextRequest = ContextRequest(Secrets4xx.TENANT, token.contextResponse.token)

            val call = retrofit.get()?.removeMoneyContainer(RemoveMoneyContainer(moneyContainer.containerId,
                    contextRequest, token.person?.id ?: ""))?.execute()
            val body = call?.body()
            if (body != null && body.contextResponse.statusCode.equals("SUCCESS"))
                moneyContainerRemoved.postValue(moneyContainer.containerId)
            else
                error.postValue(body?.contextResponse?.statusCode)
        }
    }
}