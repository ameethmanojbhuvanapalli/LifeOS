package com.lifeos.core.util

object ValidationUtils {
    fun isNonBlank(value: String?): Boolean = !value.isNullOrBlank()
}
