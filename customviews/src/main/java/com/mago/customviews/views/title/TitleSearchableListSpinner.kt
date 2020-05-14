package com.mago.customviews.views.title

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.mago.customviews.R
import com.mago.customviews.views.spinner.SearchableListSpinner

/**
 * @author by jmartinez
 * @since 11/05/2020.
 */
class TitleSearchableListSpinner: LinearLayout {
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
    var spinner: SearchableListSpinner = SearchableListSpinner(context)
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }
    // Attr
    private var isMandatory: Boolean = false
        set(value) {
            field = value
            spinner.isMandatory = value
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
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TitleSearchableListSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TitleSearchableListSpinner_isMandatory, false)
                    getString(R.styleable.TitleSearchableListSpinner_title)?.let {
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
                tvTitleText.visibility = if (it.selectedItemPosition == 0)
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

    fun setSelection(pos: Int) {
        spinner.setSelectedItems(pos)
    }

    fun getPositionOf(items: List<Any>) = spinner.getPositionOf(items)

    fun getSelectedItems() = spinner.selectedItem

    fun getAdapter() = spinner.adapter

    fun initialize(items: List<List<Any>>, title: String) {
        spinner.initialize(items, title)
    }

}