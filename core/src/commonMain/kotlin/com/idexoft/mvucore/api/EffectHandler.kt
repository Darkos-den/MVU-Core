package com.idexoft.mvucore.api

import com.idexoft.mvucore.model.effect.Effect
import com.idexoft.mvucore.model.effect.FlowEffect
import com.idexoft.mvucore.model.message.Message
import kotlinx.coroutines.flow.Flow

interface EffectHandler {
    suspend fun execute(effect: Effect): Message?
    suspend fun <T> executeFlow(effect: T): Flow<Message>? where T : Effect, T : FlowEffect = null
}