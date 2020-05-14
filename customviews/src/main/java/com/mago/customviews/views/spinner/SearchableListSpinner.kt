package com.mago.customviews.views.spinner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
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
class SearchableListSpinner: AppCompatSpinner, View.OnClickListener, View.OnTouchListener,
    SearchListSpinnerListener {
    private lateinit var attributeSet: AttributeSet
    private lateinit var searchListDialog: SearchListDialog
    private lateinit var listSelectedListener: ListSelectedListener
    private var selectedItems: List<Any> = listOf()
    private var selectedItemsPosition = -1
    private var items = listOf<List<Any>>()

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
    private var isElementSelected = false

    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var isValid = false
    /*
    var spinnerHeight: Float = 0F
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

     */
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
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.MultiSelectSearchSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.MultiSelectSearchSpinner_isMandatory, false)
                    /*spinnerHeight = getDimension(
                        R.styleable.MultiSelectSearchSpinner_spinnerHeight,
                        resources.getDimension(R.dimen.spinner_min_height)
                    )
                     */
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        isElementSelected = selectedItems.isNotEmpty()
        isValid = isElementSelected

        canvas?.apply {
            canvas.drawPath(arrowPath, arrowPaint)
            canvas.drawCircle(xOrigin + rectLarge / 2, yCenter, circleRad, circlePaint)

            background = if (!isElementSelected) {
                if (isMandatory)
                    ContextCompat.getDrawable(context, R.drawable.bg_spinner_invalid)
                else
                    ContextCompat.getDrawable(context, R.drawable.bg_spinner)
            } else
                ContextCompat.getDrawable(context, R.drawable.bg_spinner)
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
        this.selectedItems = items
        selectedItemsPosition = position
        setupAdapter(selectedItems)

        if (::listSelectedListener.isInitialized) {
            listSelectedListener.onListSelected(items)
        }
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
        searchListDialog = SearchListDialog.newInstance(title)
        searchListDialog.setListSpinnerListener(this)
        searchListDialog.setItems(items)
        setAdapter(title)
    }

    private fun showDialog() {
        val fm = scanForActivity(context)!!.supportFragmentManager
        searchListDialog.show(
            fm,
            SearchListDialog.TAG
        )
    }

    private fun setAdapter(title: String) {
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, listOf(title))
    }

    override fun getSelectedItem(): List<Any> = selectedItems

    override fun getSelectedItemPosition(): Int = selectedItemsPosition

    fun setSelectedItems(pos: Int) {
        val items = items[pos]
        selectedItemsPosition = pos
        selectedItems = items
        listSelectedListener.onListSelected(selectedItems)
        setupAdapter(selectedItems)
    }

    fun setHint(hint: String) {
        setAdapter(hint)
    }

    fun setOnListSelectedListener(listSelectedListener: ListSelectedListener) {
        this.listSelectedListener = listSelectedListener
    }

}