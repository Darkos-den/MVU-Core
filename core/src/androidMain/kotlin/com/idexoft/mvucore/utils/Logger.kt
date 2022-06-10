package com.idexoft.mvucore.utils

import android.util.Log
import com.idexoft.mvucore.api.ILogger

actual object Logger: ILogger {
    actual override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }
}