package com.idexoft.mvucore.component

import com.idexoft.mvucore.api.EffectHandler
import com.idexoft.mvucore.api.Reducer
import com.idexoft.mvucore.model.effect.Effect
import com.idexoft.mvucore.model.effect.FlowEffect
import com.idexoft.mvucore.model.effect.SingleEffect
import com.idexoft.mvucore.model.effect.effectName
import com.idexoft.mvucore.model.message.Idle
import com.idexoft.mvucore.model.state.State
import com.idexoft.mvucore.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun effectProcessor(id: Long, effect: Effect, effectHandler: EffectHandler, state: State) {
    val job = CoroutineScope(Dispatchers.Default).launch {
        if (effect is FlowEffect) {
            try {
                effectHandler.executeFlow(effect)!!.collect {
                    withContext(Dispatchers.Main) {
                        messageProcessing(id, it, state, effectHandler)
                    }
                }
            } catch (e: Exception) {
                //do nothing
            }
        } else {
            val resultMessage = effectHandler.execute(effect)
            if(resultMessage != null && resultMessage !is Idle) {
                withContext(Dispatchers.Main) {
                    messageProcessing(id, resultMessage, state, effectHandler)
                }
            }
        }
    }
    if(effect is SingleEffect) {
        globalStore?.cancelNamedJob(id, effect.effectName())
    }
    globalStore?.registerJob(id, job)
}