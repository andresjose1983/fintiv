package com.trofiventures.fintinvaccount4xx.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.trofiventures.fintinvaccount4xx.R
import com.trofiventures.fintinvaccount4xx.util.FontTypeChange


/**
 * A simple [Fragment] subclass.
 */
class CardBackFragment : Fragment() {

    lateinit var cvv: TextView
    lateinit var fontTypeChange: FontTypeChange

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_card_back, container, false)
        cvv = view.findViewById(R.id.tv_cvv)
        fontTypeChange = FontTypeChange(activity)
        cvv.typeface = fontTypeChange.get_fontface(1)

        return view
    }
}// Required empty public constructor
