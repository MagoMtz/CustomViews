package com.mago.customviews.views.multiselectspinner

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mago.customviews.R
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author by jmartinez
 * @since 20/03/2020.
 */
class MultiSpinnerAdapter(
    private val context: Context,
    private var arrayList: List<ObjectData>
): BaseAdapter(), Filterable {
    var originalValues: List<ObjectData> = arrayList
    var inflater: LayoutInflater = LayoutInflater.from(context)

    private var selected = 0
    private var limit = -1
    private lateinit var limitListener: LimitListener

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder
        val mView: View

        if (convertView == null) {
            mView = inflater.inflate(R.layout.item_listview_multiple, parent, false)
            holder = ViewHolder(
                mView.findViewById(R.id.alertTextView),
                mView.findViewById(R.id.alertCheckbox)
            )

            mView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            mView = convertView
        }

        val data = arrayList[position]

        holder.textView.text = data.name
        holder.checkBox.isChecked = data.isSelected

        convertView?.setOnClickListener {  v ->
            //if (position == 0)
            //    return@setOnClickListener
            when {
                data.isSelected -> {
                    selected--
                }
                selected == limit -> {
                    if (!::limitListener.isInitialized)
                        limitListener.onLimitListener(data)
                    return@setOnClickListener
                }
                else -> {
                    selected++
                }
            }

            val temp = v.tag as ViewHolder
            temp.checkBox.isChecked = !temp.checkBox.isChecked

            data.isSelected = !data.isSelected
            notifyDataSetChanged()
        }
        /*
        if (data.isSelected) {

        } else {

        }
        */
        holder.checkBox.tag = holder

        return mView
    }

    override fun getItem(position: Int): Any = arrayList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = arrayList.size


    override fun getFilter(): Filter {
        return object : Filter() {


            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                arrayList = results.values as List<ObjectData>
            }

            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filteredArrayList: ArrayList<ObjectData> = ArrayList()

                if (constraint == null || constraint.isEmpty()) {
                    results.count = originalValues.size
                    results.values = originalValues
                } else {
                    val mConstraint = constraint.toString().toLowerCase()
                    for (i in originalValues.indices) {
                        val data = originalValues[i].name
                        if (data.toLowerCase().contains(mConstraint)) {
                            filteredArrayList.add(originalValues[i])
                        }
                    }
                    results.count = filteredArrayList.size
                    results.values = filteredArrayList
                }
                return results
            }
        }
    }

    data class ViewHolder (
        var textView: TextView,
        var checkBox: CheckBox
    )

}