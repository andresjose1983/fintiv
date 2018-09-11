package com.trofiventures.testfintivaccounts3xx

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.trofiventures.fintivaccounts3xx.FintivAccounts3xx
import com.trofiventures.fintivaccounts3xx.viewModel.FintivAccounts3xxViewModel
import kotlinx.android.synthetic.main.activity__3xx.*

class _3xx : AppCompatActivity() {

    private lateinit var viewModel: FintivAccounts3xxViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__3xx)

        viewModel = ViewModelProviders.of(this).get(FintivAccounts3xxViewModel::class.java)

        FintivAccounts3xx.setupWithTenant(tenant = "FINTIV3.2")
        with(viewModel) {
            error.observe(this@_3xx, Observer {
                it?.let {
                    Toast.makeText(this@_3xx, it, Toast.LENGTH_SHORT).show()
                }
            })

            registration.observe(this@_3xx, Observer {
                it?.let {
                    Toast.makeText(this@_3xx, it.personRegistrationResponse.contextResponse.statusMessage, Toast.LENGTH_SHORT).show()
                }
            })

            logon.observe(this@_3xx, Observer {
                it?.let {
                    Toast.makeText(this@_3xx, "Your token ${it.signonInfo.contextResponse.token}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun register(view: View) {
        viewModel.registerUserWith(editTextName.text.toString(),
                editTextLastName.text.toString(),
                editTextUser.text.toString(),
                editTextPassword.text.toString())
    }

    fun login(view: View) {
        viewModel.login(editTextUserLogin.text.toString(),
                editTextPasswordLogin.text.toString())
    }

    fun getToken(view: View) {
        val logon = FintivAccounts3xx.currentToken(this)
        logon?.let {
            Toast.makeText(this@_3xx, "Your token ${it.signonInfo.contextResponse.token}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        with(viewModel) {
            error.removeObservers(this@_3xx)
            registration.removeObservers(this@_3xx)
            logon.removeObservers(this@_3xx)
        }
    }
}
