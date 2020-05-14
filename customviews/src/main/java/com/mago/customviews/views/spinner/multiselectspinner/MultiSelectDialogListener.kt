package com.mago.customviews.views.spinner.multiselectspinner

/**
 * @author by jmartinez
 * @since 22/03/2020.
 */
interface MultiSelectDialogListener {
    fun onCancelButton(items: List<ObjectData>)
    fun onItemsSelected(items: List<ObjectData>, selectedPos: List<Int>)
}