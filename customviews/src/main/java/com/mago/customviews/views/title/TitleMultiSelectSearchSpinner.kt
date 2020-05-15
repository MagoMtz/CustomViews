package com.mago.customviews.views.title

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mago.customviews.R
import com.mago.customviews.views.spinner.multiselectspinner.ItemsSelectedListener
import com.mago.customviews.views.spinner.MultiSelectSearchSpinner

class TitleMultiSelectSearchSpinner : LinearLayout {
    private lateinit var attributeSet: AttributeSet
    // Views
    private var tvTitleText: TextView = TextView(context)
    var title: CharSequence = ""
        set(value) {
            field = value
            tvTitleText.text = value
            requestLayout()
            invalidate()
        }
    var spinner: MultiSelectSearchSpinner =
        MultiSelectSearchSpinner(context)
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }
    // Attr
    var isMandatory: Boolean = false
        set(value) {
            field = value
            spinner.isMandatory = value
            requestLayout()
            invalidate()
        }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int):
            super(context, attributeSet, defStyleAttr) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }
    constructor(context: Context): super(context) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.title_multi_select_search_spinner, this)

        initComponents()
        setWillNotDraw(false)
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleMultiSelectSearchSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TitleMultiSelectSearchSpinner_isMandatory, false)
                    getString(R.styleable.TitleSearchableSpinner_title)?.let {
                        title = it
                    }
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            spinner.let {
                tvTitleText.visibility = if (it.getSelectedItems().isEmpty())
                    View.INVISIBLE
                else
                    View.VISIBLE
            }
        }
    }

    private fun initComponents() {
        tvTitleText = findViewById(R.id.tv_title)
        spinner = findViewById(R.id.sp_searchable)

        spinner.isMandatory = isMandatory
        tvTitleText.text = title
    }

    fun isValid(): Boolean = spinner.isValid

    fun enableViews(isEnabled: Boolean) {
        tvTitleText.isEnabled = isEnabled
        spinner.isEnabled = isEnabled
    }

    fun setSelectedItems(selectedItemPos: List<Int>) {
        spinner.setSelectedItems(selectedItemPos)
    }

    fun cleanSelection() {
        spinner.cleanSelection()
    }

    fun getSelectedItems() = spinner.getSelectedItems()

    fun getSelectedItemPositions(): List<Int> = spinner.getSelectedItemPositions()

    fun setHint(hint: String) {
        spinner.setHint(hint)
    }

    fun setOnItemsSelectedListener(listener: ItemsSelectedListener) {
        spinner.setOnItemsSelectedListener(listener)
    }

    /**
     * Use this function to initialize the spinner.
     * @param items the list of items to selection
     * @param title string to show as title
     * @param maxSelection the maximum amount of selections
     * @param minSelection the minimum amount of selections. Default value is maxSelection
     * @param overLimitMsg message who will be showing when there is no more selections available
     */
    fun init(items: List<Any>,
             title: String,
             maxSelection: Int,
             minSelection: Int = maxSelection,
             overLimitMsg: String) {
        spinner.init(items, title, maxSelection, minSelection, overLimitMsg)
    }

}