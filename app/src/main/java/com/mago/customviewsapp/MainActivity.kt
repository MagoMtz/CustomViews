package com.mago.customviewsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val items = arrayListOf(
            //"<Seleccion 0>", "<Seleccion 1>", "<Seleccion 2>", "<Seleccion 3>",
            "Seleccion 0", "Seleccion 1", "Seleccion 2", "Seleccion 3"
        )
        et_text_area.setAdapter(items)

    }

}
