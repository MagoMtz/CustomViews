package com.mago.customviews.views.spinner.searchlistspinner

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mago.customviews.R
import kotlinx.android.synthetic.main.item_listview_list.view.*

/**
 * @author by jmartinez
 * @since 15/04/2020.
 */
class SearchListAdapter(
    private var data: List<List<Any>>
) : RecyclerView.Adapter<SearchListAdapter.ViewHolder>(), Filterable,
    ListAdapter.InnerClickListener {
    private var originalValues: List<List<Any>> = data

    private lateinit var listAdapter: ListAdapter
    private lateinit var dialogListener: SearchListSpinnerListener

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_listview_list, parent, false)
        return ViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = data[position]

        setupRecyclerView(holder.itemView.rv_list, items, position)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                data = results.values as List<List<Any>>
                notifyDataSetChanged()
            }

            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filteredList: ArrayList<List<Any>> = ArrayList()

                if (constraint == null || constraint.isEmpty()) {
                    results.count = originalValues.size
                    results.values = originalValues
                } else {
                    val mConstraint = constraint.toString().toLowerCase()
                    for (i in originalValues.indices) {
                        for (j in originalValues[i].indices) {
                            if (originalValues[i][j].toString().toLowerCase().contains(mConstraint)) {
                                filteredList.add(originalValues[i])
                                break
                            }
                        }
                    }
                    val hashSet: Set<List<Any>> = LinkedHashSet(filteredList)
                    filteredList.clear()
                    filteredList.addAll(hashSet)

                    results.count = filteredList.size
                    results.values = filteredList
                }
                return results
            }
        }
    }

    override fun onListSelected(position: Int) {
        dialogListener.onItemSelected(data[position], position)
        Log.e("sdasdasd", "asasdasd")
    }

    fun setSearchSpinnerListener(listener: SearchListSpinnerListener) {
        this.dialogListener = listener
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, items: List<Any>, position: Int) {
        val data = arrayListOf<ListItem>()
        items.forEach {
            val listItem = ListItem()
            listItem.position = position
            listItem.item = it
            data.add(listItem)
        }
        listAdapter = ListAdapter(
            data,
            this
        )
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = listAdapter
        listAdapter.notifyDataSetChanged()
    }

}