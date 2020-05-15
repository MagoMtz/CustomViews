package com.mago.customviews.views.spinner.searchspinner

/**
 * @author by jmartinez
 * @since 13/05/2020.
 */
interface SearchSpinnerListener {
    fun onItemSelected(item: Any, position: Int)
    fun onCleanSelection()
}