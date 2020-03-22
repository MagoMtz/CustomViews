package com.mago.customviews.views.multiselectspinner

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import com.mago.customviews.R

/**
 * @author by jmartinez
 * @since 21/03/2020.
 */
class MultiSelectSearchSpinner(context: Context, attributeSet: AttributeSet)
    : View(context, attributeSet), View.OnClickListener {

    private lateinit var multiSelectSearchDialog: MultiSelectSearchDialog
    private lateinit var limitListener: LimitListener
    private lateinit var listener: SpinnerListener

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


        isElementSelected = 1 != 0


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

    fun initialize(items: List<Any>, listener: SpinnerListener) {
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
            "Select item"
        )
        this.listener = listener
    }

    private fun showDialog() {
        val fm = scanForActivity(context)!!.supportFragmentManager
        multiSelectSearchDialog.show(
            fm,
            MultiSelectSearchDialog.TAG
        )
        multiSelectSearchDialog.setSpinnerListener(listener)
    }

    private fun scanForActivity(context: Context): AppCompatActivity? {
        if (context is AppCompatActivity)
            return context
        else if (context is ContextWrapper)
            return scanForActivity((context).baseContext)

        return null
    }

}