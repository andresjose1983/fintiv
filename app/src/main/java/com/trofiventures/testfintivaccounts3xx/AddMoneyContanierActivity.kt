package com.trofiventures.testfintivaccounts3xx

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.trofiventures.fintinvaccount4xx.FintivAccounts4xx
import com.trofiventures.fintinvaccount4xx.viewModel.FintivAddContainer4xxViewModel
import kotlinx.android.synthetic.main.activity_add_money_contanier.*

class AddMoneyContanierActivity : AppCompatActivity() {

    lateinit var viewModel: FintivAddContainer4xxViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_money_contanier)

        FintivAccounts4xx.setupWithCardConnect("https://fts.cardconnect.com:6443/cardsecure/cs")

        viewModel = ViewModelProviders.of(this).get(FintivAddContainer4xxViewModel::class.java)

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
