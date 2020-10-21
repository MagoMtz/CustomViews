package com.mago.customviews.util

/**
 * @author by jmartinez
 * @since 15/01/2020.
 */

object RegexPattern {
    const val A_TO_Z_WITH_BLANK_SPACES = "[^A-Za-zÁ-ßáéíóúñÑ. ]+"
    const val A_TO_Z = "[^A-Za-zÁ-ßáéíóúñÑ]+"
    const val ALL_CHARS = ".+"//
    const val ONLY_NUMBERS = "[^0-9]+"
    const val DATE_TIME_PLACEHOLDER = "[^\\d.]|\\."
    const val E_MAIL = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
    const val VALID_ENTER_E_MAIL = "[A-Za-z\\!-@ñÑ_¿¡]+"
    const val DATE_DD_MM_YYYY = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}\$"
}