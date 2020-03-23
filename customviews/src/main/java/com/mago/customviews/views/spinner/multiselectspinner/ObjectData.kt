package com.mago.customviews.views.spinner.multiselectspinner

/**
 * @author by jmartinez
 * @since 20/03/2020.
 */
data class ObjectData(
    var id: Long = 0L,
    var name: String = "",
    var isSelected: Boolean = false,
    var `object`: Any = Any()
) {
    override fun toString(): String {
        return name
    }
}