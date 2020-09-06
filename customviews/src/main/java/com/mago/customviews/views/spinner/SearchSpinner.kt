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
import com.mago.customviews.views.spinner.searchspinner.ItemSelectedListener
import com.mago.customviews.views.spinner.searchspinner.SearchDialog
import com.mago.customviews.views.spinner.searchspinner.SearchSpinnerListener

/**
 * @author by jmartinez
 * @since 13/05/2020.
 */
class SearchSpinner : AppCompatSpinner, View.OnClickListener, View.OnTouchListener,
    SearchSpinnerListener {
    private lateinit var attributeSet: AttributeSet
    private lateinit var searchDialog: SearchDialog
    private lateinit var itemSelectedListener: ItemSelectedListener

    private var selectedItem: Any? = null
    private var selectedItemPosition = -1
    private var items = listOf<Any>()
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
    private var isItemSelected = false
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

    fun initialize(items: List<Any>, title: String) {
        this.title = title
        this.items = items
        searchDialog = SearchDialog.newInstance(title)
        searchDialog.setSearchSpinnerListener(this)
        searchDialog.setItems(items)
        setAdapter(title)
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.SearchSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.SearchSpinner_isMandatory, false)
                    paintArrow = getBoolean(R.styleable.SearchListSpinner_paintArrow, true)
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        isItemSelected = selectedItem != null
        isValid = isItemSelected

        canvas?.apply {
            if (paintArrow) {
                canvas.drawPath(arrowPath, arrowPaint)
                canvas.drawCircle(xOrigin + rectLarge / 2, yCenter, circleRad, circlePaint)
            }

            background = if (!isItemSelected) {
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

    override fun onItemSelected(item: Any, position: Int) {
        selectedItem = item
        selectedItemPosition = position
        if (::itemSelectedListener.isInitialized)
            itemSelectedListener.onItemSelected(item)
        setupAdapter(selectedItem.toString())
    }

    override fun onCleanSelection() {
        selectedItem = null
        selectedItemPosition = -1
        if (::itemSelectedListener.isInitialized)
            itemSelectedListener.onCleanSelection()
        setupAdapter(title)
    }

    private fun showDialog() {
        if (!::searchDialog.isInitialized)
            return

        val fm = scanForActivity(context)!!.supportFragmentManager
        searchDialog.show(
            fm,
            SearchDialog.TAG,
            isItemSelected
        )
    }

    private fun setupAdapter(item: String) {
        setAdapter(item)
    }

    private fun setAdapter(title: String) {
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, listOf(title))
    }

    override fun getSelectedItem(): Any? = selectedItem

    override fun getSelectedItemPosition(): Int = selectedItemPosition

    fun getPositionOf(item: Any) = items.indexOf(item)

    fun setSelectedItem(pos: Int) {
        val item = items[pos]
        onItemSelected(item, pos)
    }

    fun cleanSelection() {
        onCleanSelection()
    }

    fun setHint(hint: String) {
        setAdapter(hint)
    }

    fun setOnItemSelectedListener(itemSelectedListener: ItemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener
    }


}