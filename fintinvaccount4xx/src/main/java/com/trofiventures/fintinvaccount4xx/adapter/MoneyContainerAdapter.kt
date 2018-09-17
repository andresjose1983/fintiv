package com.trofiventures.fintinvaccount4xx.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trofiventures.fintinvaccount4xx.R
import com.trofiventures.fintinvaccount4xx.model.response.MoneyContainer
import com.trofiventures.fintinvaccount4xx.viewModel.FintivMoneyContainer4xxViewModel
import kotlinx.android.synthetic.main.item_money_container.view.*

class MoneyContainerAdapter(val viewModel: FintivMoneyContainer4xxViewModel) : RecyclerView.Adapter<MoneyContainerAdapter.ViewHoler>() {

    var moneyContainers = arrayListOf<MoneyContainer>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHoler {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_money_container, parent, false)
        return ViewHoler(view)
    }

    override fun getItemCount() = moneyContainers.size

    override fun onBindViewHolder(holder: ViewHoler?, position: Int) {
        holder?.let {
            it.bind(moneyContainers[position])
        }
    }

    inner class ViewHoler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(moneyContainer: MoneyContainer) {
            with(itemView) {
                moneyContainer.attributes.forEach {
                    if (it.key.equals("ACCOUNT_NAME"))
                        account_name_text_view.text = it.value
                }

                if (!moneyContainer.description.isNullOrEmpty()) {
                    description_text_view.text = moneyContainer.description
                    description_text_view.visibility = View.VISIBLE
                } else
                    description_text_view.visibility = View.GONE

                last_for_digit_text_view.text = moneyContainer.last4AccountDigits

                type_text_view.text = moneyContainer.type.plus(" ").plus(moneyContainer.subType
                        ?: "")

                itemView.setOnClickListener {
                    viewModel.delete(moneyContainer)
                }
            }
        }
    }
}