package com.mago.customviews.views.edittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import androidx.core.content.ContextCompat
import com.mago.customviews.R

/**
 * @author by jmartinez
 * @since 05/02/2020.
 */
class TextArea : AppCompatMultiAutoCompleteTextView {
    private lateinit var attributeSet: AttributeSet
    private lateinit var mTokenizer: CustomTokenizer

    private var replaceOpenCount = 0
    private var replaceCloseCount = 0
    private var openCharWrapper: Char = '<'
    private var closeCharWrapper: Char = '>'

    // Attributes
    var isMandatory: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }
    var noBackground: Boolean = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) :
            super(context, attributeSet, defStyleAttr) {
        this.attributeSet = attributeSet
        setupAttributes()
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        background = ContextCompat.getDrawable(context, R.drawable.bg_common)
        minLines = 1
        setLines(3)
        maxLines = 15
    }

    private fun setupAttributes() {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TextArea, 0, 0)
            .apply {
                try {
                    isMandatory = getBoolean(R.styleable.TextArea_isMandatory, false)
                    noBackground = getBoolean(R.styleable.TextArea_noBackground, false)
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val isNotValid = text.toString().isEmpty()

        canvas?.apply {
            val cBounds = clipBounds
            cBounds.inset(0, 8)

            if (noBackground) {
                background = null
                return@apply
            }

            val bg = background as LayerDrawable
            val gradientDrawable = bg.getDrawable(0) as GradientDrawable
            if (isMandatory)
                if (isNotValid)
                    gradientDrawable.setStroke(4, ContextCompat.getColor(context, R.color.frame_invalid))
                else
                    gradientDrawable.setStroke(4, ContextCompat.getColor(context, R.color.shadow))
            else
                gradientDrawable.setStroke(4, ContextCompat.getColor(context, R.color.shadow))

        }
    }

    fun setAdapter(data: List<String>, openCharWrapper: Char = '<', closeCharWrapper: Char = '>') {
        val adapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_dropdown_item_1line,
            data
        )

        this.openCharWrapper = openCharWrapper
        this.closeCharWrapper = closeCharWrapper

        mTokenizer = CustomTokenizer(this, openCharWrapper, closeCharWrapper)

        setAdapter(adapter)
        setTokenizer(mTokenizer)
        adapter.notifyDataSetChanged()
    }

    override fun performFiltering(text: CharSequence, keyCode: Int) {
        if (enoughToFilter()) {
            //replaceOpenCount = text.toString().toCharArray().filter { it == '<' }.count()
            //replaceOpenCount = text.toString().toCharArray().filter { it == openCharWrapper }.count()
            //replaceCloseCount = text.toString().toCharArray().filter { it == '>' }.count()
            //replaceCloseCount = text.toString().toCharArray().filter { it == closeCharWrapper }.count()

            //val aux = text.toString().replace("<", "")
            //val aux = text.toString().replace(openCharWrapper.toString(), "")
            //val mText = aux.replace(">", "")
            //val mText = aux.replace(closeCharWrapper.toString(), "")

            val end = selectionEnd //- (replaceCloseCount+replaceOpenCount)
            val start = mTokenizer.findTokenStart(text, end)
            //val start = mTokenizer.findTokenStart(mText, end)

            super.performFiltering(text, start, end, keyCode)

        }
        /*
        if (enoughToFilter()) {
            var end = selectionEnd
            var start = mTokenizer.findTokenStart(text, end)
            val mText: CharSequence

            replaceOpenCount = text.toString().toCharArray().filter { it == '<' }.count()
            replaceCloseCount = text.toString().toCharArray().filter { it == '>' }.count()

            mText = if (text[start] == '<') {
                if (text[0] == '<' && start == 0) {
                    start = 0
                    //end --
                    end -= (replaceCloseCount+replaceOpenCount)
                } else {
                    //end -= replaceCloseCount
                    //start -= replaceOpenCount
                    end -= (replaceCloseCount+replaceOpenCount)
                    start -= (replaceCloseCount+replaceOpenCount)
                }
                //end -= replaceCloseCount
                //start -= replaceOpenCount
                val aux = text.toString().replace("<", "")
                aux.replace(">", "")
            } else {
                text
            }

            super.performFiltering(mText, start, end, keyCode)
        }
         */
    }

    private class CustomTokenizer(
        private val textArea: TextArea,
        private val openCharWrapper: Char,
        private val closeCharWrapper: Char
    ) : Tokenizer {
        private var tokenStart: Int = 0
        private var tokenEnd: Int = 0

        override fun findTokenStart(text: CharSequence, cursor: Int): Int {
            var i = cursor
            while (i > 0 && text[i - 1] != ' ') {
                i--
            }
            while (i < cursor && text[i] == ' ') {
                i++
            }
            tokenStart = i
            return i
        }

        override fun findTokenEnd(text: CharSequence, cursor: Int): Int {
            var i = cursor
            val len = text.length
            while (i < len) {
                if (text[i] == ' ') {
                    return i
                } else {
                    i++
                }
            }
            tokenEnd = len
            return len
        }

        override fun terminateToken(text: CharSequence): CharSequence? {
            var i = text.length
            while (i > 0 && text[i - 1] == ' ') {
                i--
            }
            return if (i > 0 && text[i - 1] == ' ') {
                wrapWord(text)//text
            } else {
                if (text is Spanned) {
                    val sp = SpannableString("$text, ")
                    TextUtils.copySpansFrom(
                        text, 0, text.length,
                        Any::class.java, sp, 0
                    )
                    sp//wrapWord(sp)
                } else {
                    "${wrapWord(text)} "//"$text "//"${wrapWord(text)} "
                }
            }
        }

        private fun wrapWord(text: CharSequence): CharSequence {
            if (tokenStart < 0)
                return text

            //return if (textArea.text[tokenStart] == '<')
            return if (textArea.text[tokenStart] == openCharWrapper)
            //"<$text>"
                "$openCharWrapper$text$closeCharWrapper"
            else
                text
        }
    }

    fun isValid(): Boolean = text.toString().isNotEmpty()

}