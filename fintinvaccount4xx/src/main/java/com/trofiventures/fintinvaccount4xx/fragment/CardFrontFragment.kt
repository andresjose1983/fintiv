package com.trofiventures.fintinvaccount4xx.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.trofiventures.fintinvaccount4xx.R
import com.trofiventures.fintinvaccount4xx.util.FontTypeChange


/**
 * A simple [Fragment] subclass.
 */
class CardFrontFragment : Fragment() {

    var number: TextView? = null
    var name: TextView? = null
    var validity: TextView? = null
    var cardType: ImageView? = null

    private lateinit var fontTypeChange: FontTypeChange

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_card_front, container, false)
        fontTypeChange = FontTypeChange(activity)

        number = view.findViewById(R.id.tv_card_number)
        name = view.findViewById(R.id.tv_member_name)
        validity = view.findViewById(R.id.tv_validity)
        cardType = view.findViewById(R.id.ivType)

        number?.typeface = fontTypeChange.get_fontface(3)
        name?.typeface = fontTypeChange.get_fontface(3)
        validity?.typeface = fontTypeChange.get_fontface(3)


        return view
    }
}// Required empty public constructor
