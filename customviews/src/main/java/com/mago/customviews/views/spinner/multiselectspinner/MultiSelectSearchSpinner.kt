package com.mago.customviews.views.spinner.multiselectspinner

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import com.mago.customviews.R
import java.lang.StringBuilder

/**
 * @author by jmartinez
 * @since 21/03/2020.
 */
class MultiSelectSearchSpinner(context: Context, attributeSet: AttributeSet)
    : AppCompatTextView(context, attributeSet), View.OnClickListener, DialogListener {

    private lateinit var multiSelectSearchDialog: MultiSelectSearchDialog
    private lateinit var limitListener: LimitListener
    private var selectedItems: List<ObjectData> = listOf()

    private var xOrigin = 100f
    private var yOrigin = 120f
    private var yCenter = 0f
    private val rectLarge = 26.6666666667f
    private val rectHeight = 17.7777777778f
    private val circleRad = 22.2222222222f

    private lateinit var arrowPath: Path

    //Attributes
    private var isElementSelected = false

    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var spinnerHeight: Float = 0F
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var hintText: String = ""
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    // Paint objects
    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.border)
        style = Paint.Style.STROKE
        strokeWidth = 3F
    }

    private val frameAlertPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.frame_invalid)
        style = Paint.Style.STROKE
        strokeWidth = 3F
    }

    private val arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.dark_gray)
        style = Paint.Style.FILL
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.dark_gray)
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val titleTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.dark_text)
        textSize = 50f
    }

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.MultiSelectSearchSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.MultiSelectSearchSpinner_isMandatory, false)
                    spinnerHeight = getDimension(
                        R.styleable.MultiSelectSearchSpinner_spinnerHeight,
                        resources.getDimension(R.dimen.spinner_min_height)
                    )

                    background = null
                } finally {
                    recycle()
                }
            }
        setOnClickListener(this)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        isElementSelected = selectedItems.size == 2


        canvas?.apply {
            canvas.drawPath(arrowPath, arrowPaint)
            canvas.drawCircle(xOrigin + rectLarge / 2, yCenter, circleRad, circlePaint)

            if (!isElementSelected) {
                canvas.drawText(hintText, 30f, yCenter + (yCenter / 3), titleTextPaint)
                if (isMandatory)
                    drawRoundRect(clipBounds.toRectF(), 15F, 15F, frameAlertPaint)
                else
                    drawRoundRect(clipBounds.toRectF(), 15F, 15F, framePaint)
            } else
                drawRoundRect(clipBounds.toRectF(), 15F, 15F, framePaint)
        }
        /*gravity = Gravity.CENTER_VERTICAL
        setTextAppearance(context, android.R.style.TextAppearance_Medium)
        setPadding(
            CommonUtils.intToDp(context, 5),
            0,
            CommonUtils.intToDp(context, 5),
            0
        )*/
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        yCenter = h.toFloat() / 2
        xOrigin = w - xOrigin
        yOrigin = h - yOrigin

        arrowPath = Path().apply {
            val newOriginL = yCenter - (rectLarge / 4)
            moveTo(xOrigin, newOriginL)
            lineTo(xOrigin + rectLarge / 2, newOriginL + rectHeight)
            lineTo(xOrigin + rectLarge, newOriginL)
            lineTo(xOrigin, newOriginL)
            close()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val params = layoutParams
        params.height = spinnerHeight.toInt()
        requestLayout()
    }

    override fun onClick(v: View?) {
        showDialog()
    }

    override fun onItemsSelected(items: List<ObjectData>) {
        this.selectedItems = items
        val sb = StringBuilder()
        for (i in items.indices) {
            if (items[i].isSelected) {
                sb.append(items[i].name)
                sb.append(", ")
            }
        }
        val mText = sb.toString()
        text = mText.substring(0, mText.length - 2)
    }

    override fun onCancelButton(items: List<ObjectData>) {

    }

    fun initialize(items: List<Any>, title: String) {
        val data = arrayListOf<ObjectData>()
        for (i in items.indices) {
            val o = ObjectData()
            o.id = i.toLong()
            o.name = items[i].toString()
            o.isSelected = false
            o.`object` = items[i]
            data.add(o)
        }
        multiSelectSearchDialog = MultiSelectSearchDialog.newInstance(
            data,
            title
        )
        multiSelectSearchDialog.setDialogListener(this)
        text = title
    }

    private fun showDialog() {
        val fm = scanForActivity(context)!!.supportFragmentManager
        multiSelectSearchDialog.show(
            fm,
            MultiSelectSearchDialog.TAG
        )
    }

    private fun scanForActivity(context: Context): AppCompatActivity? {
        if (context is AppCompatActivity)
            return context
        else if (context is ContextWrapper)
            return scanForActivity((context).baseContext)

        return null
    }

    fun getSelectedItems(): List<ObjectData> = selectedItems

}