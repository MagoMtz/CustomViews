package com.mago.customviews.views.spinner.multiselectspinner

/**
 * @author by jmartinez
 * @since 20/03/2020.
 */
interface MultiSelectSpinnerListener {
    fun onItemClicked(items: List<ObjectData>)
    fun onItemsSelected(items: List<ObjectData>)

    fun onMinSelectionAvailable()
    fun onMinSelectionNotAvailable()
}