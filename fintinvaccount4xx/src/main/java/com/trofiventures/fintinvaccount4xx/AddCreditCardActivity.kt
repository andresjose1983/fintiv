package com.trofiventures.fintinvaccount4xx

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import com.braintreepayments.cardform.utils.CardType
import com.trofiventures.fintinvaccount4xx.fragment.CardBackFragment
import com.trofiventures.fintinvaccount4xx.fragment.CardFrontFragment
import com.trofiventures.fintinvaccount4xx.viewModel.FintivAddContainer4xxViewModel
import kotlinx.android.synthetic.main.activity_add_credit_card.*


class AddCreditCardActivity : AppCompatActivity() {

    lateinit var viewModel: FintivAddContainer4xxViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_credit_card)

        FintivAccounts4xx.setupWithCardConnect("https://fts.cardconnect.com:6443/cardsecure/cs")

        viewModel = ViewModelProviders.of(this).get(FintivAddContainer4xxViewModel::class.java)

        with(viewModel) {
            error.observe(this@AddCreditCardActivity, Observer {
                it?.let {
                    android.widget.Toast.makeText(this@AddCreditCardActivity, it, android.widget.Toast.LENGTH_SHORT).show()
                }
            })
            createAccountResponse.observe(this@AddCreditCardActivity, Observer {
                it?.let {
                    android.widget.Toast.makeText(this@AddCreditCardActivity, it.contextResponse.statusCode, android.widget.Toast.LENGTH_SHORT).show()
                }
            })
        }

        add_credit_card_form.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .maskCvv(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .setup(this)

        save_fab.setOnClickListener {
            viewModel.addCreditCard(add_credit_card_form)
        }

        val cardFrontFragment = CardFrontFragment()
        val cardBackFragment = CardBackFragment()
        // Add the fragment to the 'fragment_container' FrameLayout
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, cardFrontFragment).commit()

        add_credit_card_form.cardEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val initial = p0.toString()
                // remove all non-digits characters
                var processed = initial.replace("\\D".toRegex(), "")
                // insert a space after all groups of 4 digits that are followed by another digit
                processed = processed.replace("(\\d{4})(?=\\d)".toRegex(), "$1 ")
                // to avoid stackoverflow errors, check that the processed is different from what's already
                //  there before setting
                if (!processed.isEmpty())
                    cardFrontFragment.number?.text = processed
                else cardFrontFragment.number?.text = getString(R.string.card_number_sample)

                val cardType = CardType.forCardNumber(initial).frontResource
                cardFrontFragment.cardType?.setImageResource(cardType)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        add_credit_card_form.cvvEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        card_holder_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                cardFrontFragment.name?.text = p0.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        add_credit_card_form.expirationDateEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                cardFrontFragment.validity?.text = add_credit_card_form.expirationMonth.plus("/").plus(add_credit_card_form.expirationYear.substring(2))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        with(add_credit_card_form.cvvEditText) {
            setOnFocusChangeListener { view, b ->
                if (b) {
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.animator.card_flip_right_in,
                                    R.animator.card_flip_right_out,
                                    R.animator.card_flip_left_in,
                                    R.animator.card_flip_left_out)
                            .replace(R.id.fragment_container, cardBackFragment).commit()
                } else {
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.animator.card_flip_right_in,
                                    R.animator.card_flip_right_out,
                                    R.animator.card_flip_left_in,
                                    R.animator.card_flip_left_out)
                            .replace(R.id.fragment_container, cardFrontFragment).commit()
                }
            }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable) {
                    if (!p0.isEmpty())
                        cardBackFragment.cvv.text = p0
                    else
                        cardBackFragment.cvv.text = "XXX"
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })
        }
    }

    override fun onDestroy() {
        with(viewModel) {
            error.removeObservers(this@AddCreditCardActivity)
            createAccountResponse.removeObservers(this@AddCreditCardActivity)
        }
        super.onDestroy()
    }
}