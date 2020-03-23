package com.mago.customviews.views.spinner.multiselectspinner

/**
 * @author by jmartinez
 * @since 22/03/2020.
 */
interface DialogListener {
    fun onCancelButton(items: List<ObjectData>)
    fun onItemsSelected(items: List<ObjectData>)
}