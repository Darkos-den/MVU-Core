package com.idexoft.mvucore.utils

import com.idexoft.mvucore.api.ILogger
import platform.Foundation.NSLog

actual object Logger: ILogger {
    actual override fun d(tag: String, message: String) {
        println("[$tag] $message")
    }
}