package com.mago.customviewsapp

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.mago.customviews.views.adapter.CustomSpinnerAdapter
import com.mago.customviews.views.spinner.SearchableSpinner
import com.mago.customviews.views.spinner.multiselectspinner.ItemsSelectedListener
import com.mago.customviews.views.spinner.searchlistspinner.ListSelectedListener
import com.mago.customviews.views.spinner.searchspinner.ItemSelectedListener
import com.mago.customviews.views.title.TitleEditText
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val items = arrayListOf<String>("Seleccion 0", "Seleccion 1", "Seleccion 2", "Seleccion 3")

        var selectedList = listOf<Int>(1,3)

        SearchableSpinner(this)

        sp_multi.init(items, "Seleccione algo", 3, 2, "No mas 2")
        sp_multi.setHint("Seleccione algo123")
        sp_multi.setOnItemsSelectedListener(object : ItemsSelectedListener {
            override fun onItemsSelected(list: List<Any>) {
                list.forEach {
                    Log.e("MAIN", "item: $it")
                }
            }
        })

        sp_title_search.initialize(items, "Seleccione algo")
        sp_title_search.setOnItemSelectedListener(object : ItemSelectedListener{
            override fun onItemSelected(item: Any) {
                Log.e("tag", "item: $item")
            }
        })
        btn_set_selection_search.setOnClickListener {
            Log.e("tag", "itemPos: ${sp_title_search.getSelectedItemPosition()}")
            Log.e("tag", "item: ${sp_title_search.getSelectedItem()}")
            sp_title_search.setSelectedItem(3)
        }

        btn_set_selection.setOnClickListener {
            sp_multi.setHint("Seleccione algo123456")
            sp_title_search.setHint("Seleccione algo123456")
            Log.e("tag", "selectedItems = ${sp_multi.getSelectedItemPositions().size}")
            sp_multi.getSelectedItemPositions().forEach {
                Log.e("tag", "selectedItem = $it")
            }
        }

        btn_set_text.setOnClickListener {
            et_text.setText("asdasd")

            Log.e("ASD", "isValid: ${et_text.isValid()}")
            Log.e("ASD", "text: ${et_text.getText().toString()}")
        }

        /*
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

        val items = listOf(
            listOf("Primera lista, valor 1", "Primera lista, valor 2","Primera lista, valor 3"),
            listOf("Segunda lista, valor 1", "Segunda lista, valor 2","Segunda lista, valor 3"),
            listOf("Tercera lista, valor 1", "Tercera lista, valor 2","Tercera lista, valor 3")
        )

        list_spinner.initialize(items, "Pick")
        list_spinner.setOnListSelectedListener(object :
            ListSelectedListener {
            override fun onListSelected(list: List<Any>) {
                Log.e("Main TAG", "size: ${list.size}")
                list.forEach {
                    Log.e("Main TAG", "element: ${it}")
                }
            }
        })

         */
    }

}
