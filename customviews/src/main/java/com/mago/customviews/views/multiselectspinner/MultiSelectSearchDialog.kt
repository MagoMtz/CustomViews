package com.mago.customviews.views.multiselectspinner

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mago.customviews.R

/**
 * @author by jmartinez
 * @since 21/03/2020.
 */
class MultiSelectSearchDialog: DialogFragment() {
    private lateinit var items: List<ObjectData>
    private lateinit var title: String

    private lateinit var multiSelectSearchAdapter: MultiSelectSearchAdapter

    companion object {
        const val TAG = "MultiSelectSearchDialog"

        const val PARAM_ITEMS = "param_items"
        const val PARAM_TITLE = "param_title"

        private val listType = object : TypeToken<List<ObjectData>>() {}.type

        fun newInstance(items: List<ObjectData>, title: String): MultiSelectSearchDialog {
            val dialog = MultiSelectSearchDialog()
            val args = Bundle()

            val listJSON = Gson().toJson(items, listType)
            args.putString(PARAM_ITEMS, listJSON)
            args.putString(PARAM_TITLE, title)

            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mItems: List<ObjectData> = Gson().fromJson(arguments!!.getString(PARAM_ITEMS)!!, listType)
        items = mItems
        title = arguments!!.getString(PARAM_TITLE)!!

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity!!.layoutInflater
        val view = createView(inflater)

        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
        }

        return builder.create()
    }

    private fun createView(inflater: LayoutInflater): View {
        val view = inflater.inflate(R.layout.dialog_multi_select_search, null)

        //val title = view.findViewById<TextView>(R.id.tv_title)
        val searchView = view.findViewById<SearchView>(R.id.tv_search)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        //title.text = this.title
        setOnQueryTextChanged(searchView)
        setupRecyclerView(recyclerView)

        return view
    }


    private fun setOnQueryTextChanged(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.requestFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty())
                    multiSelectSearchAdapter.filter.filter("")
                else
                    multiSelectSearchAdapter.filter.filter(newText)

                return false
            }
        })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        multiSelectSearchAdapter = MultiSelectSearchAdapter(items)
        multiSelectSearchAdapter.setSpinnerListener(object : SpinnerListener{
            override fun onItemsSelected(items: List<ObjectData>) {
                Log.d("TAG", "selectedItems:  ${items.size}")
            }
            override fun onItemSelected(items: List<ObjectData>) {
                for (i in items.indices) {
                    if (items[i].isSelected) {
                        Log.i("TAG",
                            i.toString() + " : " + items[i].name + " : " + items[i].isSelected
                        )
                    }
                }
            }
        })

        recyclerView.adapter = multiSelectSearchAdapter
        multiSelectSearchAdapter.notifyDataSetChanged()
    }

    fun setSpinnerListener(listener: SpinnerListener) {
        //multiSelectSearchAdapter.setSpinnerListener(listener)

    }

}