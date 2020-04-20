package com.mago.customviews.views.spinner.searchlistspinner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mago.customviews.R
import kotlinx.android.synthetic.main.item_listview.view.*

/**
 * @author by jmartinez
 * @since 16/04/2020.
 */
class ListAdapter(
    private var data: List<ListItem>,
    private var listener: InnerClickListener
): RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_listview, parent, false)
        return ViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = data[position]

        holder.itemView.tv_item.text = listItem.item.toString()
        holder.itemView.tv_item.setOnClickListener {
            listener.onListSelected(listItem.position)
        }
    }

    fun setDataList(data: List<ListItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    interface InnerClickListener {
        fun onListSelected(position: Int)
    }

}