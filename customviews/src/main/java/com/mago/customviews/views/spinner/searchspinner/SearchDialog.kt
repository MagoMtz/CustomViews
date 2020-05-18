package com.mago.customviews.views.spinner.searchspinner

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
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

    private lateinit var creator: AlertDialog
    private lateinit var btnClean: Button
    private lateinit var btnCancel: Button
    private var isItemSelected = false

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

    fun show(manager: FragmentManager, tag: String?, isItemSelected: Boolean) {
        super.show(manager, tag)
        this.isItemSelected = isItemSelected
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
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        builder.setPositiveButton(R.string.btn_clean_selection) { dialog, _ ->
            //someItemSelected = false
            searchSpinnerListener.onCleanSelection()
            dialog.dismiss()
        }

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        return builder.create()
    }

    @SuppressLint("InflateParams")
    private fun createView(inflater: LayoutInflater): View {
        val view = inflater.inflate(R.layout.dialog_select_search, null)

        val searchView = view.findViewById<SearchView>(R.id.tv_search)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        searchView.onActionViewExpanded()
        setOnQueryTextChanged(searchView)
        setupRecyclerView(recyclerView)

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        return view
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        creator = dialog as AlertDialog
        btnCancel = creator.getButton(AlertDialog.BUTTON_NEGATIVE)
        btnClean = creator.getButton(AlertDialog.BUTTON_POSITIVE)

        if (!isItemSelected)
            btnClean.visibility = GONE
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
            override fun onItemSelected(item: Any, position: Int) {
                //someItemSelected = true
                searchSpinnerListener.onItemSelected(item, position)
                btnClean.visibility = View.VISIBLE
                dialog?.cancel()
            }

            override fun onCleanSelection() {}
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