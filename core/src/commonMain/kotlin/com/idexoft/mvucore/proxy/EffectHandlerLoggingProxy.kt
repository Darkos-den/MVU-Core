package com.idexoft.mvucore.proxy

import com.idexoft.mvucore.api.EffectHandler
import com.idexoft.mvucore.api.ILogger
import com.idexoft.mvucore.model.effect.Effect
import com.idexoft.mvucore.model.effect.FlowEffect
import com.idexoft.mvucore.model.message.Message
import kotlinx.coroutines.flow.Flow

class EffectHandlerLoggingProxy(
    private val effectHandler: EffectHandler,
    private val logger: ILogger
) : EffectHandler {

    override suspend fun execute(effect: Effect): Message? {
        logger.d(TAG, "[execute] effect: $effect")
        return effectHandler.execute(effect)
    }

    override suspend fun <T> executeFlow(effect: T): Flow<Message>? where T : Effect, T : FlowEffect {
        logger.d(TAG, "[executeFlow] effect: $effect")
        return effectHandler.executeFlow(effect)
    }

    companion object {
        private const val TAG = "MVU.EffectHandler"
    }
}