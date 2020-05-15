package com.mago.customviews.views.spinner.searchlistspinner

/**
 * @author by jmartinez
 * @since 16/04/2020.
 */
interface SearchListSpinnerListener {
    fun onItemSelected(itemSelected: List<Any>, position: Int)
    fun onCleanSelection()
}