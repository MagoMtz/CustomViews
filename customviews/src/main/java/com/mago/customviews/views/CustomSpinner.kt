package com.mago.customviews.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import com.mago.customviews.R

/**
 * @author by jmartinez
 * @since 17/01/2020.
 */
class CustomSpinner(context: Context, attributeSet: AttributeSet) : AppCompatSpinner(context, attributeSet) {
    private var xOrigin = 100f
    private var yOrigin = 120f
    private var yCenter = 0f
    private val rectLarge = 26.6666666667f
    private val rectHeight = 17.7777777778f
    private val circleRad = 22.2222222222f


    //Attributes
    private var isElementSelected = false

    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var titleHint: String? = ""
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var titleColor: Int = R.color.dark_text
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

    // Paint objects
    private val framePaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.border)
        style = Paint.Style.STROKE
        strokeWidth = 3F
    }

    private val frameAlertPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.frame_invalid)
        style = Paint.Style.STROKE
        strokeWidth = 3F
    }

    private val arrowPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.dark_gray)
        style = Paint.Style.FILL
    }

    private lateinit var arrowPath: Path

    private val circlePaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.dark_gray)
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val titleTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, titleColor)
        textSize = 80f
    }

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.CustomSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.CustomSpinner_isMandatory, false)
                    getString(R.styleable.CustomSpinner_titleHint)?.let { titleHint = it }
                    spinnerHeight = getDimension(
                        R.styleable.CustomSpinner_spinnerHeight,
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
                canvas.drawText(titleHint!!, 30f, yCenter + (yCenter / 3), titleTextPaint)
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

}