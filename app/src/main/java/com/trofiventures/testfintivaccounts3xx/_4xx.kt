package com.trofiventures.testfintivaccounts3xx

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.trofiventures.fintinvaccount4xx.AddCreditCardActivity
import com.trofiventures.fintinvaccount4xx.FintivAccounts4xx
import com.trofiventures.fintinvaccount4xx.viewModel.FintivAccounts4xxViewModel
import kotlinx.android.synthetic.main.activity__3xx.*

class _4xx : AppCompatActivity() {

    private lateinit var viewModel: FintivAccounts4xxViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__4xx)

        viewModel = ViewModelProviders.of(this).get(FintivAccounts4xxViewModel::class.java)

        FintivAccounts4xx.setupWithCardConnect("https://fts.cardconnect.com:6443/cardsecure/cs")
        FintivAccounts4xx.setupWithTenant(tenant = "JANUS")

        with(viewModel) {
            error.observe(this@_4xx, Observer {
                it?.let {
                    Toast.makeText(this@_4xx, it, Toast.LENGTH_SHORT).show()
                }
            })
            personCreated.observe(this@_4xx, Observer {
                it?.let {
                    Toast.makeText(this@_4xx, "Person created with id ${it.person.id}", Toast.LENGTH_SHORT).show()
                }
            })
            addCredentialResponse.observe(this@_4xx, Observer {
                it?.let {
                    Toast.makeText(this@_4xx, "Credentials added for this person ${it.personId}", Toast.LENGTH_SHORT).show()
                }
            })
            sigonResponse.observe(this@_4xx, Observer {
                it?.let {
                    Toast.makeText(this@_4xx, "Your token ${it.contextResponse.token}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun login(view: View) {
        viewModel.sigon(editTextUserLogin.text.toString(),
                editTextPasswordLogin.text.toString())
    }

    fun register(view: View) {
        viewModel.registerUserWith(editTextName.text.toString(),
                editTextLastName.text.toString(),
                editTextUser.text.toString(),
                editTextPassword.text.toString())
    }

    fun getToken(view: View) {
        val token = FintivAccounts4xx.currentToken(this)
        token.let {
            Toast.makeText(this, "Your token ${it?.contextResponse?.token}", Toast.LENGTH_SHORT).show()
        }
    }

    fun sva(view: View) {
        startActivity(Intent(this, CreateAccount4xxActivity::class.java))
    }

    fun cc(view: View) {
        //startActivity(Intent(this, AddMoneyContanierActivity::class.java))
        startActivity(Intent(this, AddCreditCardActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        with(viewModel) {
            error.removeObservers(this@_4xx)
            personCreated.removeObservers(this@_4xx)
            addCredentialResponse.removeObservers(this@_4xx)
            sigonResponse.removeObservers(this@_4xx)
        }
    }
}
