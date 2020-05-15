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
import com.mago.customviews.views.spinner.multiselectspinner.ItemsSelectedListener
import com.mago.customviews.views.spinner.multiselectspinner.MultiSelectDialogListener
import com.mago.customviews.views.spinner.multiselectspinner.MultiSelectSearchDialog
import com.mago.customviews.views.spinner.multiselectspinner.ObjectData

/**
 * @author by jmartinez
 * @since 21/03/2020.
 */
class MultiSelectSearchSpinner : AppCompatSpinner, View.OnClickListener, View.OnTouchListener,
    MultiSelectDialogListener {
    private lateinit var attributeSet: AttributeSet
    private lateinit var multiSelectSearchDialog: MultiSelectSearchDialog
    private lateinit var itemsSelectedListener: ItemsSelectedListener

    private var selectedItems: List<ObjectData> = listOf()
    private var selectedItemPositions: List<Int> = listOf()
    private var items = listOf<ObjectData>()
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
    private var isElementSelected = false

    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var maxSelection: Int = 0
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var minSelection: Int = 0
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
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.MultiSelectSearchSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.MultiSelectSearchSpinner_isMandatory, false)
                    minSelection = getInteger(R.styleable.MultiSelectSearchSpinner_minSelection, 0)
                    maxSelection = getInteger(R.styleable.MultiSelectSearchSpinner_maxSelection, 0)
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        isElementSelected = selectedItems.size >= minSelection
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

    override fun onItemsSelected(items: List<ObjectData>, selectedPos: List<Int>) {
        this.selectedItems = items
        this.selectedItemPositions = selectedPos
        setupAdapter(items)

        if (::itemsSelectedListener.isInitialized) {
            val arrayList = arrayListOf<Any>()
            for (i in items.indices) {
                arrayList.add(items[i].item)
            }
            itemsSelectedListener.onItemsSelected(arrayList)
        }
    }

    override fun onCancelButton(items: List<ObjectData>) {
        setupAdapter(items)
    }

    override fun onCleanSelection() {
        selectedItems = listOf()
        selectedItemPositions = listOf()
        setAdapter(title)
        if (::itemsSelectedListener.isInitialized) {
            itemsSelectedListener.onCleanSelection()
        }
    }

    /**
     * Use this function to initialize the spinner.
     * @param items the list of items to selection
     * @param title string to show as title
     * @param maxSelection the maximum amount of selections
     * @param minSelection the minimum amount of selections. Default value is maxSelection
     * @param overLimitMsg message who will be showing when there is no more selections available
     */
    fun init(
        items: List<Any>,
        title: String,
        maxSelection: Int,
        minSelection: Int = maxSelection,
        overLimitMsg: String
    ) {
        initialize(items, title, maxSelection, minSelection, overLimitMsg)
    }

    private fun initialize(items: List<Any>, title: String, maxSelection: Int, minSelection: Int, overLimitMsg: String) {
        this.minSelection = minSelection
        this.maxSelection = maxSelection
        this.title = title

        val data = arrayListOf<ObjectData>()
        for (i in items.indices) {
            val o =
                ObjectData()
            o.id = i.toLong()
            o.name = items[i].toString()
            o.isSelected = false
            o.item = items[i]
            data.add(o)
        }
        multiSelectSearchDialog =
            MultiSelectSearchDialog.newInstance(
                title,
                overLimitMsg,
                maxSelection,
                minSelection
            )
        multiSelectSearchDialog.setDialogListener(this)
        multiSelectSearchDialog.setItems(data)
        setAdapter(title)

        this.items = ArrayList(data)
    }

    private fun showDialog() {
        val fm = scanForActivity(context)!!.supportFragmentManager
        multiSelectSearchDialog.show(
            fm,
            MultiSelectSearchDialog.TAG
        )
    }

    private fun setupAdapter(items: List<ObjectData>) {
        if (items.isEmpty())
            return

        val sb = StringBuilder()
        for (i in items.indices) {
            if (items[i].isSelected) {
                sb.append(items[i].name)
                sb.append(", ")
            }
        }
        val mText = sb.toString()
        setAdapter(mText.substring(0, mText.length - 2))
    }

    private fun setAdapter(title: String) {
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, listOf(title))
    }

    fun getSelectedItems(): List<Any> {
        val arrayList = arrayListOf<Any>()
        selectedItems.forEach {
            arrayList.add(it.item)
        }
        return arrayList
    }

    fun getSelectedItemPositions(): List<Int> = selectedItemPositions

    fun setSelectedItems(selectedItemPos: List<Int>) {
        val mItems = arrayListOf<ObjectData>()
        val mPos = arrayListOf<Int>()

        selectedItemPos.forEach {
            items[it].isSelected = true
            mItems.add(items[it])
            mPos.add(it)
        }

        onItemsSelected(mItems, mPos)
        //multiSelectSearchDialog.setSelectedItems(selectedItemPos)
        //selectedItemPositions = selectedItemPos
    }

    fun cleanSelection() {
        onCleanSelection()
    }

    fun setHint(hint: String) {
        setAdapter(hint)
    }

    fun setOnItemsSelectedListener(listener: ItemsSelectedListener) {
        itemsSelectedListener = listener
    }

}