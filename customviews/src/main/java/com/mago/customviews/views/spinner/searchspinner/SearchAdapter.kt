package com.mago.customviews.views.spinner.searchspinner

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.mago.customviews.R
import kotlinx.android.synthetic.main.item_list.view.*

/**
 * @author by jmartinez
 * @since 13/05/2020.
 */
class SearchAdapter(
    private var data: List<Any>
): RecyclerView.Adapter<SearchAdapter.ViewHolder>(), Filterable {
    private var originalValues: List<Any> = data

    private lateinit var spinnerListener: SearchSpinnerListener

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.itemView.tv_item.text = item.toString()
        holder.itemView.setOnClickListener {
            spinnerListener.onItemSelected(item)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                data = results.values as List<Any>
                notifyDataSetChanged()
            }
            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filteredList: ArrayList<Any> = ArrayList()

                if (constraint == null || constraint.isEmpty()) {
                    results.count = originalValues.size
                    results.values = originalValues
                } else {
                    val mConstraint = constraint.toString().toLowerCase()
                    for (i in originalValues.indices) {
                        val item = originalValues[i].toString()
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

    fun setSpinnerListener(spinnerListener: SearchSpinnerListener) {
        this.spinnerListener = spinnerListener
    }

    fun getItems() = data

}