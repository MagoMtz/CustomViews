package com.mago.customviews.views.spinner.multiselectspinner

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mago.customviews.R
import kotlinx.android.synthetic.main.dialog_select_search.*
import kotlinx.android.synthetic.main.dialog_select_search.view.*

/**
 * @author by jmartinez
 * @since 21/03/2020.
 */
class MultiSelectSearchDialog: DialogFragment() {
    private lateinit var items: List<ObjectData>
    private lateinit var title: String
    private lateinit var overLimitMsg: String
    private var maxSelection: Int = 0
    private var minSelection: Int = 0

    private lateinit var multiSelectSearchAdapter: MultiSelectSearchAdapter
    private lateinit var listener: MultiSelectDialogListener
    private lateinit var btnSelect: Button
    private lateinit var btnCancel: Button
    private lateinit var viewAux: View

    private var selected = 0
    private var itemsSelected = arrayListOf<ObjectData>()

    companion object {
        const val TAG = "MultiSelectSearchDialog"

        const val PARAM_TITLE = "param_title"
        const val PARAM_OVER_LIMIT_MSG = "param_over_limit_msg"
        const val PARAM_MAX_SELECTION = "param_max_selection"
        const val PARAM_MIN_SELECTION = "param_min_selection"

        fun newInstance(title: String, overLimitMsg: String, maxSelection: Int, minSelection: Int): MultiSelectSearchDialog {
            val dialog = MultiSelectSearchDialog()
            val args = Bundle()

            args.putString(PARAM_TITLE, title)
            args.putString(PARAM_OVER_LIMIT_MSG, overLimitMsg)
            args.putInt(PARAM_MAX_SELECTION, maxSelection)
            args.putInt(PARAM_MIN_SELECTION, minSelection)

            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments!!.getString(PARAM_TITLE)!!
        overLimitMsg = arguments!!.getString(PARAM_OVER_LIMIT_MSG)!!
        maxSelection = arguments!!.getInt(PARAM_MAX_SELECTION)
        minSelection = arguments!!.getInt(PARAM_MIN_SELECTION)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity!!.layoutInflater
        val view = createView(inflater)

        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setView(view)

        return builder.create()
    }

    private fun createView(inflater: LayoutInflater): View {
        val view = inflater.inflate(R.layout.dialog_select_search, null)

        val searchView = view.findViewById<SearchView>(R.id.tv_search)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        searchView.onActionViewExpanded()
        setOnQueryTextChanged(searchView)
        setupRecyclerView(recyclerView)

        viewAux =  view.aux

        btnCancel = view.btn_cancel
        btnCancel.setOnClickListener {
            if (::listener.isInitialized) {
                listener.onCancelButton(itemsSelected)
                dialog?.dismiss()
            }
        }
        btnSelect = view.btn_select
        btnSelect.setOnClickListener {
            if (::listener.isInitialized) {
                listener.onItemsSelected(itemsSelected)
                dialog?.dismiss()
            }
        }

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
        multiSelectSearchAdapter = MultiSelectSearchAdapter(items, selected, itemsSelected)
        multiSelectSearchAdapter.setSpinnerListener(object : MultiSelectSpinnerListener{
            override fun onItemsSelected(items: List<ObjectData>) {
                Log.d("TAG", "selectedItems:  ${items.size}")
                listener.onItemsSelected(items)

                selected = items.size
                itemsSelected = ArrayList(items)
                dialog?.cancel()
            }
            override fun onItemClicked(items: List<ObjectData>) {
                for (i in items.indices) {
                    if (items[i].isSelected) {
                        Log.i("TAG",
                            i.toString() + " : " + items[i].name + " : " + items[i].isSelected
                        )
                    }
                }
                Log.d("TAG", "selectedItems:  ${items.size}")
                selected = items.size
                itemsSelected = ArrayList(items)
            }

            override fun onMinSelectionAvailable() {
                val params = viewAux.layoutParams as LinearLayout.LayoutParams
                params.weight = .5F
                viewAux.layoutParams = params

                btnSelect.visibility = VISIBLE
            }

            override fun onMinSelectionNotAvailable() {
                btnSelect.visibility = GONE

                val params = viewAux.layoutParams as LinearLayout.LayoutParams
                params.weight = .75F
                viewAux.layoutParams = params
            }
        })
        multiSelectSearchAdapter.setLimitListener(object : LimitListener {
            override fun onLimitListener(data: ObjectData) {
                Toast.makeText(context, overLimitMsg, Toast.LENGTH_SHORT).show()
                dialog?.cancel()
            }
        }, maxSelection, minSelection)

        recyclerView.adapter = multiSelectSearchAdapter
        multiSelectSearchAdapter.notifyDataSetChanged()
    }

    fun setDialogListener(listener: MultiSelectDialogListener) {
        this.listener = listener
    }

    fun setItems(items: List<ObjectData>) {
        this.items = items
    }

}