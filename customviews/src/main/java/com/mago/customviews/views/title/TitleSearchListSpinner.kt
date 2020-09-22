package com.mago.customviews.views.title

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mago.customviews.R
import com.mago.customviews.views.spinner.SearchListSpinner
import com.mago.customviews.views.spinner.searchlistspinner.ListSelectedListener

/**
 * @author by jmartinez
 * @since 11/05/2020.
 */
class TitleSearchListSpinner: LinearLayout {
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
    var spinner: SearchListSpinner = SearchListSpinner(context)
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
    var paintArrow: Boolean = true
        set(value) {
            field = value
            spinner.paintArrow = value
            requestLayout()
            invalidate()
        }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int)
            : super(context, attributeSet, defStyleAttr) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }
    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.title_searchable_list_spinner, this)

        initComponents()
        setWillNotDraw(false)
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleSearchListSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TitleSearchListSpinner_isMandatory, false)
                    paintArrow = getBoolean(R.styleable.TitleSearchListSpinner_paintArrow, true)
                    getString(R.styleable.TitleSearchListSpinner_title)?.let {
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
        spinner.paintArrow = paintArrow
        tvTitleText.text = title
    }

    fun isValid(): Boolean = spinner.isValid

    fun enableViews(isEnabled: Boolean) {
        tvTitleText.isEnabled = isEnabled
        spinner.isEnabled = isEnabled
    }

    fun setSelection(pos: Int) {
        spinner.setSelectedItems(pos)
    }

    fun cleanSelection() {
        spinner.cleanSelection()
    }

    fun getPositionOf(items: List<Any>) = spinner.getPositionOf(items)

    fun getSelectedItems() = spinner.getSelectedItems()

    fun initialize(items: List<List<Any>>, title: String) {
        spinner.initialize(items, title)
    }

    fun setOnListSelectedListener(listSelectedListener: ListSelectedListener) {
        spinner.setOnListSelectedListener(listSelectedListener)
    }

    fun setHint(hint: String) {
        spinner.setHint(hint)
    }

}