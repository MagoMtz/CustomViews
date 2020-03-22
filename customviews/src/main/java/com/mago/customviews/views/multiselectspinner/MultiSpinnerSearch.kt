package com.mago.customviews.views.multiselectspinner

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.widget.AppCompatSpinner
import com.mago.customviews.R

/**
 * @author by jmartinez
 * @since 20/03/2020.
 */
class MultiSpinnerSearch(context: Context, attributeSet: AttributeSet):
    AppCompatSpinner(context, attributeSet), DialogInterface.OnCancelListener {

    companion object {
        private const val TAG = "MultiSpinnerSearch"
        lateinit var builder: AlertDialog.Builder
        lateinit var alertDialog: AlertDialog
    }

    private var limit = -1
    private var selected = 0
    private var defaultText = ""
    private var spinnerTitle = ""
    private var emptyTitle = "Not Found!"
    private var searchHint = "Type to search"
    private var colorSeparation = false

    private var isMandatory = false
    private var spinnerHeight = 0F

    private lateinit var listener: SpinnerListener
    private lateinit var limitListener: LimitListener

    private lateinit var adapter: MultiSpinnerAdapter
    private lateinit var items: List<ObjectData>

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.MultiSpinnerSearch, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.MultiSelectSpinner_isMandatory, false)
                    spinnerHeight = getDimension(
                        R.styleable.MultiSelectSpinner_spinnerHeight,
                        resources.getDimension(R.dimen.spinner_min_height)
                    )

                    background = null
                } finally {
                    recycle()
                }
            }
    }

    fun isColorSeparation() = colorSeparation
    fun setColorSeparation(colorSeparation: Boolean) {
        this.colorSeparation = colorSeparation
    }
    fun getHintText(): String = spinnerTitle
    fun setHintText(hintText: String?) {
        spinnerTitle = hintText!!
    }
    fun setLimit(limit: Int, listener: LimitListener) {
        this.limit = limit
        limitListener = listener
    }
    fun setEmptyTitle(emptyTitle: String) {
        this.emptyTitle = emptyTitle
    }
    fun setSearchHint(searchHint: String) {
        this.searchHint = searchHint
    }

    override fun onCancel(dialog: DialogInterface?) {
        val sb = StringBuilder()

        for (i in items.indices) {
            if (items[i].isSelected) {
                sb.append(items[i].name)
                sb.append(", ")
            }
        }

        var spinnerText = sb.toString()
        spinnerText = if (spinnerText.length > 2)
            spinnerText.substring(0, spinnerText.length -2)
        else
            this.getHintText()

        val adapterSpinner = ArrayAdapter<String>(
            context,
            android.R.layout.simple_list_item_1,
            arrayListOf(spinnerText)
        )
        super.setAdapter(adapterSpinner)

        //if (!::adapter.isInitialized)
        //    adapter.notifyDataSetChanged()

        listener.onItemSelected(items)
    }

    override fun performClick(): Boolean {
        super.performClick()

        builder = AlertDialog.Builder(context)
        builder.setTitle(spinnerTitle)

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.searchable_list_dialog, null)
        builder.setView(view)

        val listView = view.findViewById<ListView>(R.id.listItems)
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        listView.isFastScrollEnabled = false
        adapter = MultiSpinnerAdapter(context, items)
        listView.adapter = adapter

        val searchView = view.findViewById<SearchView>(R.id.search)
        searchView.queryHint = searchHint
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        builder.setPositiveButton(android.R.string.ok) { d, _ ->
            d.cancel()
        }

        builder.setOnCancelListener(this)
        alertDialog = builder.show()
        alertDialog.window?.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        return true
    }

    fun initialize(items: List<ObjectData>, position: Int, listener: SpinnerListener) {
        this.items = items
        this.listener = listener

        val sb = java.lang.StringBuilder()

        for (i in items.indices) {
            if (items[i].isSelected) {
                sb.append(items[i].name)
                sb.append(", ")
            }
        }
        if (sb.length > 2)
            defaultText = sb.toString().substring(0, sb.toString().length - 2)

        val adapterSpinner = ArrayAdapter<String>(
            context,
            android.R.layout.simple_list_item_1,
            listOf(defaultText)
        )
        super.setAdapter(adapterSpinner)

        if (position != 0) {
            items[position].isSelected = true
            onCancel(null)
        }

    }

}