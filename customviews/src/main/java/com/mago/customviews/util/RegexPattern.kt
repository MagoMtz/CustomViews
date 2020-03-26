package com.mago.customviews.util

/**
 * @author by jmartinez
 * @since 15/01/2020.
 */

object RegexPattern {
    const val A_TO_Z_WITH_BLANK_SPACES = "[A-ZÁ-ß ]+"
    const val A_TO_Z = "[A-ZÁ-ß]+"
    const val ALL_CHARS = ".+"//"[A-ZÁ-ß0-9 ]+"
    const val ONLY_NUMBERS = "[0-9]+"
    const val DATE = "[^\\d.]|\\."
    const val E_MAIL = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
    const val VALID_ENTER_E_MAIL = "[A-Za-z\\!-@ñÑ_¿¡]+"
}