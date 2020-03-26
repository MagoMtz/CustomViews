package com.mago.customviewsapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mago.customviews.views.adapter.CustomSpinnerAdapter
import com.mago.customviews.views.spinner.multiselectspinner.ItemsSelectedListener
import com.mago.customviews.views.title.TitleEditText
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = CustomSpinnerAdapter(
            this,
            arrayListOf("Mi spinner", "Seleccion 1", "Seleccion 2", "Seleccion 3"),
            "Mi spinner"
        )

        sp_biographic.spinner.adapter = adapter

        sp_biographic.spinner.isMandatory = true

        val searchAdapter = CustomSpinnerAdapter(
            this,
            arrayListOf("", "Seleccion 1", "Seleccion 2", "Seleccion 3"),
            "fff"
        )

        sp_title_search.spinner.adapter = searchAdapter
        sp_title_search.spinner
            .setSelection(2)

        /*
        val multiSelectAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayListOf("", "Seleccion 1", "Seleccion 2", "Seleccion 3")
        )
         */

        val array = arrayListOf("Seleccion 1", "Seleccion 2", "Seleccion 3")

        sp_multi.spinner.init(array, "Pick", 2, "Sólo se pueden seleccionar 2 elementos")
        sp_multi.spinner.setOnItemsSelectedListener(object : ItemsSelectedListener {
            override fun onItemsSelected(list: List<Any>) {
                list.forEach {
                    Log.d("TAG", "element: $it")
                }
            }
        })

        sp.spinner.init(array, "Pick", 2, "Sólo se pueden seleccionar 2 elementos")


        val titleET = TitleEditText(this)
        titleET.titleHint = "Programmatically"
        ly_container.addView(titleET)

    }

}
