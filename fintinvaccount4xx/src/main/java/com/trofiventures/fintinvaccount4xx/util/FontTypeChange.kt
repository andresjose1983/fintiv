package com.trofiventures.fintinvaccount4xx.util

import android.content.Context
import android.graphics.Typeface

class FontTypeChange(private val c: Context?) {

    fun get_fontface(n: Int): Typeface {
        val tf: Typeface
        if (n == 1)
            tf = Typeface.createFromAsset(c?.assets, "fonts/kreditback.ttf")
        else if (n == 2)
            tf = Typeface.createFromAsset(c?.assets, "fonts/kreditfront.ttf")
        else if (n == 3)
            tf = Typeface.createFromAsset(c?.assets, "fonts/ocramedium.ttf")
        else
            tf = Typeface.createFromAsset(c?.assets, "fonts/kreditfront.ttf")
        return tf
    }

}
