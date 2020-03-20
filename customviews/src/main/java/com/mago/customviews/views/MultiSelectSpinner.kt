package com.mago.customviews.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.graphics.toRectF
import com.mago.customviews.R
import com.toptoche.searchablespinnerlibrary.SearchableListDialog

/**
 * @author by jmartinez
 * @since 18/03/2020.
 */
class MultiSelectSpinner(context: Context, attributeSet: AttributeSet) :
    SearchableSpinner(context, attributeSet, true), SearchableListDialog.MultipleItem {

    private var selectedItems: ArrayList<Any> = ArrayList()
    private var items: ArrayList<Any>? = null
    var originalAdapter: ArrayAdapter<Any>? = null
    var previousAdapter: ArrayAdapter<Any>? = null
    private var isValid = false

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.MultiSelectSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.MultiSelectSpinner_isMandatory, false)
                    spinnerHeight = getDimension(
                        R.styleable.MultiSelectSpinner_spinnerHeight,
                        resources.getDimension(R.dimen.spinner_min_height)
                    )

                    background = null
                } finally {
                    recycle()
                }
            }
        searchableListDialog.setOnMultipleItemClickListener(this)
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        isValid = selectedItems.size == 2
        isElementSelected = isValid
        //super.onDraw(canvas)

        canvas?.apply {
            canvas.drawPath(arrowPath, arrowPaint)
            canvas.drawCircle(xOrigin + rectLarge / 2, yCenter, circleRad, circlePaint)

            if (!isValid) {
                canvas.drawText(hintText, 30f, yCenter + (yCenter / 3), titleTextPaint)
                if (isMandatory)
                    drawRoundRect(clipBounds.toRectF(), 15F, 15F, frameAlertPaint)
                else
                    drawRoundRect(clipBounds.toRectF(), 15F, 15F, framePaint)
            } else
                drawRoundRect(clipBounds.toRectF(), 15F, 15F, framePaint)
        }
    }

    override fun onItemSelected(o: Any) {
        //selectedItems.add(o)
    }

    override fun onItemUnselected(o: Any) {
        //selectedItems.remove(o)
    }

    override fun onItemsSelected(items: String, selectedItems: List<Any>) {
        adapter = ArrayAdapter(context,
            android.R.layout.simple_list_item_1,
            listOf(items))
        previousAdapter = adapter as ArrayAdapter<Any>
        this.selectedItems = ArrayList(selectedItems)
    }

    override fun onSelectionCanceled() {
        //selectedItems.clear()
        adapter = previousAdapter
        invalidate()
    }

    private fun init() {
        items = ArrayList()
        setOnTouchListener(this)
        searchableListDialog.setPositiveButton("Close") { _, _ -> onSelectionCanceled()}
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            adapter = originalAdapter
        }
        return super.onTouch(v, event)
    }

    fun getSelectedItems(): ArrayList<Any> = selectedItems

}