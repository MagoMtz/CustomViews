package com.mago.customviews.views.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.mago.customviews.R

/**
 * This adapter must have the first element of its data as a placeholder (Empty String)
 *
 * @author by jmartinez
 * @since 17/01/2020.
 * @param title is used to display the titleHint of spinner
 */
class CustomSpinnerAdapter<T : Any>(
    context: Context,
    data: List<T>,
    private val title: String
) : ArrayAdapter<T>(context, R.layout.custom_spinner_item, data) {

    private val hidingItemIndex = 0

    init {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (position == hidingItemIndex) {
            val mView = super.getDropDownView(position, convertView, parent)
            mView!!.findViewById<TextView>(android.R.id.text1).text = title
            mView
        } else {
            super.getDropDownView(position, convertView, parent)
        }
    }

}