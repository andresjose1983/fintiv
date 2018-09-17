package com.trofiventures.fintinvaccount4xx

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.trofiventures.fintinvaccount4xx.adapter.MoneyContainerAdapter
import com.trofiventures.fintinvaccount4xx.viewModel.FintivMoneyContainer4xxViewModel
import kotlinx.android.synthetic.main.activity_get_money_container.*

class GetMoneyContainerActivity : AppCompatActivity() {

    private lateinit var viewModelMoneyContainers: FintivMoneyContainer4xxViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_money_container)

        viewModelMoneyContainers = ViewModelProviders.of(this).get(FintivMoneyContainer4xxViewModel::class.java)

        val adapter = MoneyContainerAdapter(viewModelMoneyContainers)
        viewModelMoneyContainers.moneyContainers.observe(this, Observer {
            it?.let {
                money_container_recycler_view.adapter = adapter
                money_container_recycler_view.layoutManager = LinearLayoutManager(this)
                val dividerItemDecoration = DividerItemDecoration(money_container_recycler_view.context,
                        (money_container_recycler_view.layoutManager as LinearLayoutManager).orientation)
                money_container_recycler_view.addItemDecoration(dividerItemDecoration)
                adapter.moneyContainers.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModelMoneyContainers.moneyContainerRemoved.observe(this, Observer {
            it?.let {
                val mcRemoved = adapter.moneyContainers.find { mc ->
                    it.equals(mc.containerId)
                }
                adapter.moneyContainers.remove(mcRemoved)
                adapter.notifyDataSetChanged()
            }
        })

        viewModelMoneyContainers.getMoneyContainers()
    }

    override fun onDestroy() {
        viewModelMoneyContainers.moneyContainers.removeObservers(this)
        viewModelMoneyContainers.moneyContainerRemoved.removeObservers(this)
        super.onDestroy()
    }
}
