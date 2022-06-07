package com.idexoft.mvucore

import com.idexoft.mvucore.api.EffectHandler
import com.idexoft.mvucore.model.effect.Effect
import com.idexoft.mvucore.model.effect.FlowEffect
import com.idexoft.mvucore.model.message.Message
import kotlinx.coroutines.flow.Flow

class MainEffectHandler: EffectHandler {

    private var effectHandlers = emptyList<EffectHandler>()

    fun registerEffectHandler(effectHandler: EffectHandler) {
        effectHandlers = effectHandlers + effectHandler
    }

    override suspend fun execute(effect: Effect): Message {
        effectHandlers.forEach {
            it.execute(effect)?.let {
                return it
            }
        }
        throw IllegalArgumentException("effect handler for effect [${effect::class.simpleName}] not registered!")
    }

    override suspend fun <T> executeFlow(effect: T): Flow<Message> where T : Effect, T : FlowEffect {
        effectHandlers.forEach {
            it.executeFlow(effect)?.let {
                return it
            }
        }
        throw IllegalArgumentException("effect handler for effect [${effect::class.simpleName}] not registered!")
    }
}