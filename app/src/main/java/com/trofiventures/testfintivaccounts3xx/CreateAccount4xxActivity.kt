package com.trofiventures.testfintivaccounts3xx

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.trofiventures.fintinvaccount4xx.viewModel.FintivSVA4xxViewModel
import kotlinx.android.synthetic.main.activity_create_account4xx.*

class CreateAccount4xxActivity : AppCompatActivity() {

    lateinit var viewModel: FintivSVA4xxViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account4xx)

        viewModel = ViewModelProviders.of(this).get(FintivSVA4xxViewModel::class.java)

        viewModel.error.observe(this@CreateAccount4xxActivity, Observer {
            it?.let {
                Toast.makeText(this@CreateAccount4xxActivity, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.createAccountResponse.observe(this@CreateAccount4xxActivity, Observer {
            it?.let {
                Toast.makeText(this@CreateAccount4xxActivity, it.contextResponse.statusCode, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun save(view: View) {
        viewModel.createAccount(account_name_edit_text.text.toString(), "840")
    }

    override fun onDestroy() {
        viewModel.error.removeObservers(this)
        viewModel.createAccountResponse.removeObservers(this)
        super.onDestroy()
    }
}
