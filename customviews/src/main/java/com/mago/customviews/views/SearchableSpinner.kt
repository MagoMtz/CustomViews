package com.mago.customviews.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import com.mago.customviews.R

/**
 * @author by jmartinez
 * @since 04/02/2020.
 */
class SearchableSpinner(context: Context, attributeSet: AttributeSet) :
    com.toptoche.searchablespinnerlibrary.SearchableSpinner(context, attributeSet) {
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
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.SearchableSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.SearchableSpinner_isMandatory, false)
                    spinnerHeight = getDimension(
                        R.styleable.SearchableSpinner_spinnerHeight,
                        resources.getDimension(R.dimen.spinner_min_height)
                    )

                    background = null
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        isElementSelected = selectedItemPosition != 0


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
/*
        val params = layoutParams
        params.height = spinnerHeight.toInt()
        //layoutParams = params
        requestLayout()
 */
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
        //setMeasuredDimension(params.width, params.height)
    }

}