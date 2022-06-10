package com.idexoft.mvucore.utils

import com.idexoft.mvucore.api.ILogger

expect object Logger: ILogger {

	override fun d(tag: String, message: String)
}