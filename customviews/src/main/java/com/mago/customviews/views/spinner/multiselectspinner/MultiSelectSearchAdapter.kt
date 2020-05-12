package com.mago.customviews.views.spinner.multiselectspinner

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.mago.customviews.R
import kotlinx.android.synthetic.main.item_listview_multiple.view.*

/**
 * @author by jmartinez
 * @since 21/03/2020.
 */
class MultiSelectSearchAdapter(
    private var data: List<ObjectData>,
    private var selected: Int,
    private var itemsSelected: ArrayList<ObjectData>
) :
    RecyclerView.Adapter<MultiSelectSearchAdapter.ViewHolder>(), Filterable {
    private var originalValues: List<ObjectData> = data
    private var maxSelection = 0
    private var minSelection = 0

    //private var selected = 0
    //private val itemsSelected = arrayListOf<ObjectData>()

    private lateinit var limitListener: LimitListener
    private lateinit var listener: MultiSelectSpinnerListener

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_listview_multiple, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.itemView.alertTextView.text = item.name
        holder.itemView.alertCheckbox.isChecked = item.isSelected

        if (position == data.size -1)
            if (selected >= minSelection) {
                if (::listener.isInitialized)
                    listener.onMinSelectionAvailable()
            } else {
                if (::listener.isInitialized)
                    listener.onMinSelectionNotAvailable()
            }

        holder.itemView.setOnClickListener {
            when {
                item.isSelected -> {
                    selected--
                    itemsSelected.remove(item)
                }
                selected == maxSelection -> {
                    if (::limitListener.isInitialized)
                        limitListener.onLimitListener(item)
                    return@setOnClickListener
                }
                else -> {
                    selected++
                    itemsSelected.add(item)

                    /*
                    if (::listener.isInitialized)
                        listener.onItemClicked(itemsSelected)

                     */
                }
            }

            val temp = holder.itemView
            temp.alertCheckbox.isChecked = !temp.alertCheckbox.isChecked
            item.isSelected = !item.isSelected
            notifyDataSetChanged()

            if (::listener.isInitialized)
                listener.onItemClicked(itemsSelected)

            if (selected == maxSelection) {
                if (::listener.isInitialized) {
                    listener.onItemsSelected(itemsSelected)
                }
            }
        }

    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                data = results.values as List<ObjectData>
                notifyDataSetChanged()
            }

            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filteredList: ArrayList<ObjectData> = ArrayList()

                if (constraint == null || constraint.isEmpty()) {
                    results.count = originalValues.size
                    results.values = originalValues
                } else {
                    val mConstraint = constraint.toString().toLowerCase()
                    for (i in originalValues.indices) {
                        val item = originalValues[i].name
                        if (item.toLowerCase().contains(mConstraint)) {
                            filteredList.add(originalValues[i])
                        }
                    }
                    results.count = filteredList.size
                    results.values = filteredList
                }
                return results
            }


        }
    }

    fun setSpinnerListener(listener: MultiSelectSpinnerListener) {
        this.listener = listener
    }

    fun setLimitListener(limitListener: LimitListener, maxSelection: Int, minSelection: Int) {
        this.limitListener = limitListener
        this.maxSelection = maxSelection
        this.minSelection = minSelection
    }

}