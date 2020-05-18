package com.mago.customviews.views.spinner.multiselectspinner

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
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
import kotlin.math.min

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

    private lateinit var creator: AlertDialog
    private lateinit var btnCancel: Button
    private lateinit var btnClean: Button
    private lateinit var btnSelect: Button

    private var selected = 0
    private var itemsSelected = arrayListOf<ObjectData>()
    private var selectedItemPositions = arrayListOf<Int>()

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
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            if (::listener.isInitialized) {
                listener.onCancelButton(itemsSelected)
            }
            dialog.dismiss()
        }
        builder.setNeutralButton(R.string.btn_clean_selection) { _ , _ ->
            selected = 0
            itemsSelected = arrayListOf()
            multiSelectSearchAdapter.cleanSelection()
            if (::listener.isInitialized) {
                listener.onCleanSelection()
            }
        }
        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            if (::listener.isInitialized) {
                listener.onItemsSelected(itemsSelected, selectedItemPositions)
            }
            dialog.dismiss()
        }

        return builder.create()
    }

    @SuppressLint("InflateParams")
    private fun createView(inflater: LayoutInflater): View {
        val view = inflater.inflate(R.layout.dialog_select_search, null)

        val searchView = view.findViewById<SearchView>(R.id.tv_search)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        //searchView.onActionViewExpanded()
        setOnQueryTextChanged(searchView)
        setupRecyclerView(recyclerView)

        return view
    }

    override fun onStart() {
        super.onStart()
        creator = dialog as AlertDialog
        btnCancel = creator.getButton(AlertDialog.BUTTON_NEGATIVE)
        btnClean = creator.getButton(AlertDialog.BUTTON_NEUTRAL)
        btnSelect = creator.getButton(AlertDialog.BUTTON_POSITIVE)

        if (itemsSelected.size == 0) {
            btnClean.visibility = GONE
        }
        if (itemsSelected.size < minSelection) {
            btnSelect.visibility = GONE
        }

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
            override fun onItemsSelected(items: List<ObjectData>, selectedItemPos: List<Int>) {
                listener.onItemsSelected(items, selectedItemPos)

                selected = items.size
                itemsSelected = ArrayList(items)
                selectedItemPositions = ArrayList(selectedItemPos)
                dialog?.cancel()
            }
            override fun onItemClicked(items: List<ObjectData>, selectedItemPos: List<Int>) {
                selected = items.size
                itemsSelected = ArrayList(items)
                selectedItemPositions = ArrayList(selectedItemPos)

                if (itemsSelected.size > 0)
                    btnClean.visibility = VISIBLE
                else
                    btnClean.visibility = GONE

                if (itemsSelected.size >= minSelection) {
                    btnSelect.visibility = VISIBLE
                } else {
                    btnSelect.visibility = GONE
                }
            }

            override fun onCleanSelection() {}
        })
        multiSelectSearchAdapter.setLimitListener(object : LimitListener {
            override fun onLimitListener(data: ObjectData) {
                Toast.makeText(context, overLimitMsg, Toast.LENGTH_SHORT).show()
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

    fun getItems(): List<ObjectData> = items

    fun setSelectedItems(selectedItemPos: List<Int>) {
        val selectedItems = arrayListOf<ObjectData>()

        selectedItemPos.forEach {
            items[it].isSelected = true
            selectedItems.add(items[it])
        }
        selectedItemPositions = ArrayList(selectedItemPos)
        if (selectedItems.size >= minSelection || selectedItems.size == maxSelection) {
            itemsSelected = selectedItems
            selected = itemsSelected.size
            if (::listener.isInitialized)
                listener.onItemsSelected(selectedItems, selectedItemPos)
        }

    }

}