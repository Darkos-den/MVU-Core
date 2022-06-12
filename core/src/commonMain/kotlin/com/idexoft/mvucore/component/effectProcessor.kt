package com.idexoft.mvucore.component

import com.idexoft.mvucore.api.EffectHandler
import com.idexoft.mvucore.api.Reducer
import com.idexoft.mvucore.model.effect.Effect
import com.idexoft.mvucore.model.message.Idle
import com.idexoft.mvucore.model.state.State
import com.idexoft.mvucore.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun effectProcessor(id: Long, effect: Effect, effectHandler: EffectHandler, state: State) {
    CoroutineScope(Dispatchers.Default).launch {
        val resultMessage = effectHandler.execute(effect)
        Logger.d("SKA", "----| result: ${resultMessage}")
        if(resultMessage != null && resultMessage !is Idle) {
            withContext(Dispatchers.Main) {
                messageProcessing(id, resultMessage, state, effectHandler)
            }
        }
    }
}