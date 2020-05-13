package com.mago.customviews.views.spinner.searchspinner

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mago.customviews.R
import kotlinx.android.synthetic.main.dialog_select_search.view.*

/**
 * @author by jmartinez
 * @since 13/05/2020.
 */
class SearchDialog: DialogFragment() {
    private lateinit var items: List<Any>
    private lateinit var title: String

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchSpinnerListener: SearchSpinnerListener

    companion object {
        const val TAG = "SearchDialog"

        const val PARAM_TITLE = "param_title"

        fun newInstance(title: String): SearchDialog {
            val dialog = SearchDialog()
            val args = Bundle()

            args.putString(PARAM_TITLE, title)

            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments!!.getString(PARAM_TITLE)!!
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

        view.btn_cancel.setOnClickListener {
            dialog?.dismiss()
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
                    searchAdapter.filter.filter("")
                else
                    searchAdapter.filter.filter(newText)

                return false
            }
        })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        searchAdapter = SearchAdapter(items)
        searchAdapter.setSpinnerListener(object : SearchSpinnerListener {
            override fun onItemSelected(item: Any) {
                searchSpinnerListener.onItemSelected(item)
                dialog?.cancel()
            }
        })
        recyclerView.adapter = searchAdapter
        searchAdapter.notifyDataSetChanged()
    }

    fun setSearchSpinnerListener(searchSpinnerListener: SearchSpinnerListener) {
        this.searchSpinnerListener = searchSpinnerListener
    }

    fun setItems(items: List<Any>) {
        this.items = items
    }

    fun getItems() = items

}