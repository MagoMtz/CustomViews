package com.mago.customviews.views.spinner.searchlistspinner

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mago.customviews.R
import com.mago.customviews.views.spinner.searchspinner.SearchDialog

/**
 * @author by jmartinez
 * @since 16/04/2020.
 */
class SearchListDialog: DialogFragment() {
    private lateinit var items: List<List<Any>>
    private lateinit var title: String

    private lateinit var searchListAdapter: SearchListAdapter
    private lateinit var listener: SearchListSpinnerListener

    private lateinit var creator: AlertDialog
    private lateinit var btnClean: Button
    private lateinit var btnCancel: Button

    companion object {
        const val TAG = "SearchDialog"
        const val PARAM_TITLE = "param_title"

        var someItemSelected = false

        fun newInstance(title: String): SearchListDialog {
            val d =
                SearchListDialog()
            val args = Bundle()

            args.putString(PARAM_TITLE, title)

            d.arguments = args
            return d
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments!!.getString(PARAM_TITLE)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity!!.layoutInflater
        val view = createView(inflater)

        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setView(view)
        builder.setNegativeButton(android.R.string.cancel) {dialog, _ ->
            dialog.dismiss()
        }
        builder.setPositiveButton(R.string.btn_clean_selection) { dialog, which ->
            someItemSelected = false
            listener.onCleanSelection()
            dialog.dismiss()
        }

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        return builder.create()
    }

    @SuppressLint("InflateParams")
    private fun createView(inflater: LayoutInflater): View {
        val view = inflater.inflate(R.layout.dialog_select_search, null)

        val searchView:SearchView = view.findViewById(R.id.tv_search)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)

        searchView.onActionViewExpanded()
        setOnQueryTextChanged(searchView)
        setupRecyclerView(recyclerView)

        return view
    }

    override fun onStart() {
        super.onStart()
        creator = dialog as AlertDialog
        btnCancel = creator.getButton(AlertDialog.BUTTON_NEGATIVE)
        btnClean = creator.getButton(AlertDialog.BUTTON_POSITIVE)

        if (!SearchDialog.someItemSelected)
            btnClean.visibility = View.GONE
    }

    private fun setOnQueryTextChanged(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.requestFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty())
                    searchListAdapter.filter.filter("")
                else
                    searchListAdapter.filter.filter(newText)

                return false
            }
        })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        searchListAdapter =
            SearchListAdapter(
                items
            )
        searchListAdapter.setSearchSpinnerListener(object :
            SearchListSpinnerListener {
            override fun onItemSelected(itemSelected: List<Any>, position: Int) {
                someItemSelected = true
                listener.onItemSelected(itemSelected, position)
                btnCancel.visibility = View.VISIBLE
                dismiss()
            }

            override fun onCleanSelection() {}
        })
        recyclerView.adapter = searchListAdapter
        searchListAdapter.notifyDataSetChanged()
    }

    fun setListSpinnerListener(listener: SearchListSpinnerListener) {
        this.listener = listener
    }

    fun setItems(items: List<List<Any>>) {
        this.items = items
    }

}