package com.trofiventures.testfintivaccounts3xx

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun _3xx(view: View) {
        startActivity(Intent(this, _3xx::class.java))
    }

    fun _4xx(view: View){
        startActivity(Intent(this, _4xx::class.java))
    }

}
