package com.mago.customviews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mago.customviews.views.adapter.CustomSpinnerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = CustomSpinnerAdapter(
            this,
            arrayListOf("", "Seleccion 1", "Seleccion 2"),
            "Mi spinner"
        )

        sp_biographic.spinner?.adapter = adapter

        sp_biographic.spinner?.isMandatory = true
        sp_biographic.spinner?.titleHint = "Mi spinner 2"

        val searchAdapter = CustomSpinnerAdapter(
            this,
            arrayListOf("Seleccion 1", "Seleccion 2"),
            "fff"
        )

        sp_title_search.spinner?.adapter = searchAdapter
        sp_title_search.spinner?.isMandatory = true

    }
}
