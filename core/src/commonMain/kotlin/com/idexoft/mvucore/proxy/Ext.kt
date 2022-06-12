package com.idexoft.mvucore.proxy

import com.idexoft.mvucore.api.*
import com.idexoft.mvucore.utils.Logger

fun IComponent.wrapLoggingProxy(logger: ILogger = Logger) = ComponentLoggingProxy(this, logger)

fun EffectHandler.wrapLoggingProxy(logger: ILogger = Logger) = EffectHandlerLoggingProxy(this, logger)

fun Reducer.wrapLoggingProxy(logger: ILogger = Logger) = ReducerLoggingProxy(this, logger)

fun IStateStore.wrapLoggingProxy(logger: ILogger = Logger) = StateStoreLoggingProxy(this, logger)