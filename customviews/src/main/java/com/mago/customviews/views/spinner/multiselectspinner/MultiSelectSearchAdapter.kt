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
class MultiSelectSearchAdapter(private var data: List<ObjectData>):
    RecyclerView.Adapter<MultiSelectSearchAdapter.ViewHolder>(), Filterable {
    private var originalValues: List<ObjectData> = data
    private var selected = 0
    private var limit = -1

    private lateinit var limitListener: LimitListener
    private lateinit var listener: SpinnerListener
    private val itemsSelected = arrayListOf<ObjectData>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

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

        holder.itemView.setOnClickListener {
            when {
                item.isSelected -> {
                    selected--
                    itemsSelected.remove(item)
                }
                selected == limit -> {
                    if (::limitListener.isInitialized)
                        limitListener.onLimitListener(item)
                    return@setOnClickListener
                }
                else -> {
                    selected++
                    itemsSelected.add(item)

                    if (::listener.isInitialized)
                        listener.onItemSelected(itemsSelected)
                }
            }

            val temp = holder.itemView
            temp.alertCheckbox.isChecked = !temp.alertCheckbox.isChecked
            item.isSelected = !item.isSelected
            notifyDataSetChanged()

            if (selected == 2) {// TODO: cambiar 2 por
                if (::listener.isInitialized) {
                    listener.onItemsSelected(itemsSelected)
                    // TODO: cerrar dialogo
                }
            }
        }

    }

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

    fun setSpinnerListener(listener: SpinnerListener) {
        this.listener = listener
    }

}