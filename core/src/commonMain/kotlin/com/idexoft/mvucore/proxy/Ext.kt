package com.idexoft.mvucore.proxy

import com.idexoft.mvucore.api.*

fun IComponent.wrapLoggingProxy(logger: ILogger) = ComponentLoggingProxy(this, logger)

fun EffectHandler.wrapLoggingProxy(logger: ILogger) = EffectHandlerLoggingProxy(this, logger)

fun Reducer.wrapLoggingProxy(logger: ILogger) = ReducerLoggingProxy(this, logger)

fun IStateStore.wrapLoggingProxy(logger: ILogger) = StateStoreLoggingProxy(this, logger)