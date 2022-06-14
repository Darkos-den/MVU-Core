package com.idexoft.mvucore.component

import com.idexoft.mvucore.api.EffectHandler
import com.idexoft.mvucore.api.IStateStore
import com.idexoft.mvucore.api.Reducer
import com.idexoft.mvucore.model.effect.FlowEffect
import com.idexoft.mvucore.model.effect.None
import com.idexoft.mvucore.model.message.Idle
import com.idexoft.mvucore.model.message.Message
import com.idexoft.mvucore.model.state.State
import com.idexoft.mvucore.utils.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

var globalReducer: Reducer? = null
var globalStore: IStateStore? = null
var globalStateListener: StateListener? = null

fun messageProcessing(id: Long, message: Message, state: State, effectHandler: EffectHandler) {
    if (message is Idle) {
        return
    }
    val reducer = globalReducer!!
    val stateEffect = reducer.reduce(message, state)
    val globalStore = globalStore!!

    if (stateEffect == null) {
        state.parentIdState.id?.let { parentId ->
            globalStore.getOrNull(parentId)?.let {
                messageProcessing(parentId, message, it, effectHandler)
            } ?: run {
                IllegalArgumentException("parent id for state $state not found!")
            }
        } ?: run {
            IllegalArgumentException("reducer for message [${message}] not registered!")
        }
    } else {
        globalStore.updateState(id, stateEffect.state)
        globalStateListener?.invoke(id, state)

        stateEffect.effects.filter {
            it !is None
        }.forEach {
            effectProcessor(id, it, effectHandler, stateEffect.state)
        }
    }
}