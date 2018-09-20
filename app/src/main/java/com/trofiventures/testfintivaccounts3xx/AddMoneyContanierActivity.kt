package com.trofiventures.testfintivaccounts3xx

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.trofiventures.fintinvaccount4xx.FintivAccounts4xx
import com.trofiventures.fintinvaccount4xx.viewModel.FintivMoneyContainer4xxViewModel

class AddMoneyContanierActivity : AppCompatActivity() {

    lateinit var viewModel: FintivMoneyContainer4xxViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_money_contanier)

        FintivAccounts4xx.setupWithCardConnect("https://fts.cardconnect.com:6443/cardconnect/", "496160873888", "testing", "testing123")
        FintivAccounts4xx.setupWithTenant(tenant = "JANUS")

        viewModel = ViewModelProviders.of(this).get(FintivMoneyContainer4xxViewModel::class.java)

        with(viewModel) {
            error.observe(this@AddMoneyContanierActivity, Observer {
                it?.let {
                    Toast.makeText(this@AddMoneyContanierActivity, it, Toast.LENGTH_SHORT).show()
                }
            })
            createAccountResponse.observe(this@AddMoneyContanierActivity, Observer {
                it?.let {
                    Toast.makeText(this@AddMoneyContanierActivity, it.contextResponse.statusCode, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun save(view: View) {
        //viewModel.addCreditCard()
    }
}
