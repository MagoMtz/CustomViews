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
class SearchableSpinner(context: Context, attributeSet: AttributeSet): com.toptoche.searchablespinnerlibrary.SearchableSpinner(context, attributeSet) {
    private var xOrigin = 100f
    private var yOrigin = 120f
    private var yCenter = 0f
    private val rectLarge = 60f
    private val rectHeight = 40f
    private val circleRad = 50f

    private var isElementSelected = false

    //Attributes
    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var tittle: String? = ""
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var tittleColor: Int = R.color.dark_text
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    // Paint objects
    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.dark_gray)
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

    private lateinit var arrowPath: Path

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.dark_gray)
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    private val tittleTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, tittleColor)
        textSize = 80f
    }

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.SearchableSpinner, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.SearchableSpinner_isMandatory, false)
                    tittle = getString(R.styleable.SearchableSpinner_title)

                    background = null
                } finally {
                    recycle()
                }
            }


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        isElementSelected = selectedItemPosition != -1

        canvas?.apply {
            canvas.drawPath(arrowPath, arrowPaint)
            canvas.drawCircle(xOrigin + rectLarge / 2, yCenter, circleRad, circlePaint)

            if (!isElementSelected) {
                if (tittle == null) tittle = ""

                canvas.drawText(tittle!!, 30f, yCenter + (yCenter / 3), tittleTextPaint)
                if (isMandatory)
                    drawRoundRect(clipBounds.toRectF(), 15F, 15F, frameAlertPaint)
                else
                    drawRoundRect(clipBounds.toRectF(), 15F, 15F, framePaint)
            }else
                drawRoundRect(clipBounds.toRectF(), 15F, 15F, framePaint)
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        yCenter = h.toFloat() / 2
        xOrigin = w - xOrigin
        yOrigin = h - yOrigin

        arrowPath = Path().apply {
            val newOriginL = yCenter - (rectLarge / 4 )
            moveTo(xOrigin, newOriginL)
            lineTo(xOrigin + rectLarge / 2, newOriginL + rectHeight)
            lineTo(xOrigin + rectLarge, newOriginL)
            lineTo(xOrigin, newOriginL)
            close()
        }
    }

}