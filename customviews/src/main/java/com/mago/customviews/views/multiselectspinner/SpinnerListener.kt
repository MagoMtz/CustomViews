package com.mago.customviews.views.multiselectspinner

/**
 * @author by jmartinez
 * @since 20/03/2020.
 */
interface SpinnerListener {
    fun onItemSelected(items: List<ObjectData>)
    fun onItemsSelected(items: List<ObjectData>)
}