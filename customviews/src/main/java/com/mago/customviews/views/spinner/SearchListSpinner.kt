package com.mago.customviews.views.spinner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import com.mago.customviews.R
import com.mago.customviews.util.CommonUtils.scanForActivity
import com.mago.customviews.views.spinner.searchlistspinner.ListSelectedListener
import com.mago.customviews.views.spinner.searchlistspinner.SearchListDialog
import com.mago.customviews.views.spinner.searchlistspinner.SearchListSpinnerListener
import java.lang.StringBuilder

/**
 * @author by jmartinez
 * @since 15/04/2020.
 */
class SearchListSpinner: AppCompatSpinner, View.OnClickListener, View.OnTouchListener,
    SearchListSpinnerListener {
    private lateinit var attributeSet: AttributeSet
    private lateinit var searchListDialog: SearchListDialog
    private lateinit var listSelectedListener: ListSelectedListener

    private var selectedItems: List<Any> = listOf()
    private var selectedItemsPosition = -1
    private var items = listOf<List<Any>>()
    private lateinit var title: String

    private var xOrigin = 100f
    private var yOrigin = 120f
    private var yCenter = 0f
    private val rectLarge = 26.6666666667f
    private val rectHeight = 17.7777777778f
    private val circleRad = 22.2222222222f
    private val heightOrigin = 120F
    private val widthOrigin = 100F

    private lateinit var arrowPath: Path

    //Attributes
    private var areItemsSelected = false

    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var paintArrow: Boolean = true
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var isValid = false
    // Paint objects
    private val arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.dark_gray)
        style = Paint.Style.FILL
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.dark_gray)
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }
    constructor(context: Context): super(context) {
        init()
    }

    private fun init() {
        setOnTouchListener(this)
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.SearchListSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.SearchListSpinner_isMandatory, false)
                    paintArrow = getBoolean(R.styleable.SearchListSpinner_paintArrow, true)
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        areItemsSelected = selectedItems.isNotEmpty()
        isValid = areItemsSelected

        canvas?.apply {
            if (paintArrow) {
                canvas.drawPath(arrowPath, arrowPaint)
                canvas.drawCircle(xOrigin + rectLarge / 2, yCenter, circleRad, circlePaint)
            }

            val bg = background as LayerDrawable
            val gradientDrawable = bg.getDrawable(0) as GradientDrawable
            if (!areItemsSelected) {
                if (isMandatory)
                    gradientDrawable.setStroke(4, ContextCompat.getColor(context, R.color.frame_invalid))
                else
                    gradientDrawable.setStroke(4, ContextCompat.getColor(context, R.color.shadow))
            } else
                gradientDrawable.setStroke(4, ContextCompat.getColor(context, R.color.shadow))
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        yCenter = h.toFloat() / 2
        xOrigin = w - widthOrigin
        yOrigin = h - heightOrigin

        arrowPath = Path().apply {
            val newOriginL = yCenter - (rectLarge / 4)
            moveTo(xOrigin, newOriginL)
            lineTo(xOrigin + rectLarge / 2, newOriginL + rectHeight)
            lineTo(xOrigin + rectLarge, newOriginL)
            lineTo(xOrigin, newOriginL)
            close()
        }
    }

    override fun onClick(v: View?) {
        showDialog()
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP)
            showDialog()
        return true
    }

    override fun onItemSelected(itemSelected: List<Any>, position: Int) {
        this.selectedItems = itemSelected
        selectedItemsPosition = position
        setupAdapter(selectedItems)

        if (::listSelectedListener.isInitialized) {
            listSelectedListener.onListSelected(selectedItems)
        }
    }

    override fun onCleanSelection() {
        selectedItems = listOf()
        selectedItemsPosition = -1
        if (::listSelectedListener.isInitialized)
            listSelectedListener.onCleanSelection()
        setupAdapter(listOf(title))
    }

    private fun setupAdapter(items: List<Any>) {
        val sb = StringBuilder()

        for (i in items.indices) {
            sb.append(items[i])
            if (i < items.size - 1)
                sb.append("\n")
        }
        val mText = sb.toString()
        setAdapter(mText)
    }

    fun initialize(items: List<List<Any>>, title: String) {
        this.title = title
        searchListDialog = SearchListDialog.newInstance(title)
        searchListDialog.setListSpinnerListener(this)
        searchListDialog.setItems(items)
        this.items = items
        setAdapter(title)
    }

    private fun showDialog() {
        if (!::searchListDialog.isInitialized)
            return

        val fm = scanForActivity(context)!!.supportFragmentManager
        searchListDialog.show(
            fm,
            SearchListDialog.TAG,
            areItemsSelected
        )
    }

    private fun setAdapter(title: String) {
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, listOf(title))
    }

    fun getSelectedItems(): List<Any> = selectedItems

    fun getSelectedItemsPosition(): Int = selectedItemsPosition

    fun getPositionOf(item: List<Any>) = items.indexOf(item)

    fun setSelectedItems(pos: Int) {
        val items = items[pos]
        onItemSelected(items, pos)
    }

    fun cleanSelection() {
        onCleanSelection()
    }

    fun setHint(hint: String) {
        setAdapter(hint)
    }

    fun setOnListSelectedListener(listSelectedListener: ListSelectedListener) {
        this.listSelectedListener = listSelectedListener
    }

}