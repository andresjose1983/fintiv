package com.trofiventures.fintivaccounts.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Base64
import com.trofiventures.fintivaccounts.FintivAccounts
import com.trofiventures.fintivaccounts.model.AccountItem
import com.trofiventures.fintivaccounts.model.FintivPerson
import com.trofiventures.fintivaccounts.model.FintivToken
import com.trofiventures.fintivaccounts.services.RetrofitClient
import com.trofiventures.fintivaccounts.util.Secrets
import okhttp3.ResponseBody
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.util.*


class FintivAccountsViewModel(private val context: Application) : AndroidViewModel(context) {

    private val retrofit = RetrofitClient()
    var fintivPerson: MutableLiveData<FintivPerson> = MutableLiveData() // only for register
    var fintivToken: MutableLiveData<FintivToken> = MutableLiveData() // only for login or (register + login)
    var error: MutableLiveData<String> = MutableLiveData() // both (register, login)

    /**
     *  use this method to register a new person also set canLogin=true to login right away
     */
    fun register(firstName: String,
                 lastName: String,
                 password: String,
                 username: String,
                 canLogin: Boolean = false) {

        if (Secrets.WALLETID.isEmpty()) {
            error.value = "error_wallet_id"
            return
        }

        if (firstName.isEmpty()) {
            error.value = "error_first_name"
            return
        }

        if (lastName.isEmpty()) {
            error.value = "error_last_name"
            return
        }

        if (password.isEmpty()) {
            error.value = "error_password"
            return
        }

        if (username.isEmpty()) {
            error.value = "error_username"
            return
        }

        doAsync {
            val call = retrofit.get()?.signUp(firstName, lastName, password, username, Secrets.WALLETID)?.execute()
            val fpr = call?.body()
            if (fpr != null)
                when (canLogin) {
                    true -> login(username, password, fpr)
                    else -> fintivPerson.postValue(fpr)
                }
            else
                handlerError(call?.errorBody())
        }
    }

    /**
     *fp parameter is only used when person is registered and login right away
     */
    fun login(user: String,
              password: String,
              fp: FintivPerson? = null) {

        if (Secrets.CLIENTID.isEmpty()) {
            error.value = "error_client_id"
            return
        }

        if (Secrets.CLIENTSECRET.isEmpty()) {
            error.value = "error_client_secret"
            return
        }

        if (user.isEmpty()) {
            error.value = "error_username"
            return
        }

        if (password.isEmpty()) {
            error.value = "error_password"
            return
        }

        doAsync {

            val credentials = Secrets.CLIENTID.plus(":").plus(Secrets.CLIENTSECRET)
            // create Base64 encodet string
            val basic = "Basic ".plus(Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP))

            val call = retrofit.get()?.login(auth = basic,
                    gt = "password",
                    password = password,
                    username = user)?.execute()
            val ftr = call?.body()
            if (ftr != null) {
                FintivAccounts.saveToken(context, ftr)
                if (fp != null) {
                    ftr.fintivPerson = fp
                    fintivToken.postValue(ftr)
                } else
                    getAccountInfo(ftr)
            } else
                handlerError(call?.errorBody())
        }
    }

    /**
     * update account info
     */
    fun updateAccountInfo(lastName: String,
                          firstName: String,
                          password: String,
                          username: String) {
        if (Secrets.WALLETID.isEmpty()) {
            error.value = "error_wallet_id"
            return
        }

        if (firstName.isEmpty()) {
            error.value = "error_first_name"
            return
        }

        if (lastName.isEmpty()) {
            error.value = "error_last_name"
            return
        }

        if (password.isEmpty()) {
            error.value = "error_password"
            return
        }

        if (username.isEmpty()) {
            error.value = "error_username"
            return
        }

        val ft = FintivAccounts.getToken(context)
        if (ft == null) {
            error.value = "invalid_token"
            return
        }

        doAsync {

            val accountItem = AccountItem(lastName, firstName, password, username)

            val call = retrofit.get()?.updateAccountInfo("Bearer ${ft.access_token}",
                    accountItem, ft.fintivPerson?.personId?:"")?.execute()
            val fpr = call?.body()
            if (fpr != null) {

            } else
                handlerError(call?.errorBody())
        }
    }

    private fun getAccountInfo(ft: FintivToken) {
        doAsync {
            val cfp = retrofit.get()?.getAccountInfo("Bearer ${ft.access_token}")?.execute()
            val fp = cfp?.body()
            val json = JSONObject(fp as AbstractMap<String, Any>)
            json.let {
                val details = it.getJSONObject("details")
                ft.fintivPerson = FintivPerson((details["id"] as Double).toInt(),
                        details["username"].toString(),
                        details["password"].toString(),
                        details["personId"].toString(),
                        details["firstName"].toString(),
                        details["lastName"].toString(),
                        details["walletId"].toString(),
                        details["enabled"] as Boolean,
                        details["accountNonExpired"] as Boolean,
                        details["accountNonLocked"] as Boolean,
                        details["credentialsNonExpired"] as Boolean)
                FintivAccounts.saveToken(context, ft)
                fintivToken.postValue(ft)
            }
        }
    }

    private fun handlerError(responseBody: ResponseBody?) {
        val jsonError = JSONObject(responseBody?.string())
        if (jsonError.has("message"))
            error.postValue(jsonError.getString("message"))
        if (jsonError.has("error_description"))
            error.postValue(jsonError.getString("error_description"))
    }
}