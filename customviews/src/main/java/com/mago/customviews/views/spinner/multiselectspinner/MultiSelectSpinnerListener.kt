package com.mago.customviews.views.spinner.multiselectspinner

/**
 * @author by jmartinez
 * @since 20/03/2020.
 */
interface MultiSelectSpinnerListener {
    fun onItemClicked(items: List<ObjectData>, selectedItemPos: List<Int>)
    fun onItemsSelected(items: List<ObjectData>, selectedItemPos: List<Int>)
    fun onCleanSelection()

}