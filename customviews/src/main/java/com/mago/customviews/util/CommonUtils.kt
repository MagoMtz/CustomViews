package com.mago.customviews.util

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object CommonUtils {

    fun getDate(date: String): Date {
        val f = SimpleDateFormat(Constants.STRING_TO_DATE_FORMAT, Locale.getDefault())
        return f.parse(date)!!
    }

    fun getDaysDiff(date1: Date, date2: Date): Long {
        val diff = date1.time - date2.time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }

    fun getDaysSum(date: Date, days: Int): Long {
        //val sum = date1.time + date2.time
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, days)
        return c.timeInMillis
    }

    fun intToDp(context: Context, dp: Int): Int {
        return (context.resources.displayMetrics.density * dp + 0.5F).toInt()
    }

    fun scanForActivity(context: Context): AppCompatActivity? {
        if (context is AppCompatActivity)
            return context
        else if (context is ContextWrapper)
            return scanForActivity((context).baseContext)

        return null
    }

}