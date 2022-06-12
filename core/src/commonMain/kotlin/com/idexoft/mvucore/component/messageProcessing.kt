package com.idexoft.mvucore.component

import co.touchlab.stately.isFrozen
import com.idexoft.mvucore.api.EffectHandler
import com.idexoft.mvucore.api.IStateStore
import com.idexoft.mvucore.api.Reducer
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
        state.parentIdState.access {
            it.id
        }?.let { parentId ->
            Logger.d("SKA", "----| 1 stateStore: ${globalStore.isFrozen}")
            globalStore.getOrNull(parentId)?.let {
                messageProcessing(parentId, message, it, effectHandler)
            } ?: run {
                IllegalArgumentException("parent id for state $state not found!")
            }
        } ?: run {
            IllegalArgumentException("reducer for message [${message}] not registered!")
        }
    } else {
        Logger.d("SKA", "----| 2 stateStore: ${globalStore.isFrozen}")
        globalStore.updateState(id, stateEffect.state)
        globalStateListener?.invoke(id, state)

        stateEffect.effects.filter {
            it !is None
        }.forEach {
            effectProcessor(id, it, effectHandler, stateEffect.state)
            runBlocking { delay(1000) }
            Logger.d("SKA", "----| reducer: ${reducer.isFrozen}")
            Logger.d("SKA", "----| effectHandler: ${effectHandler.isFrozen}")
            Logger.d("SKA", "----| stateStore: ${globalStore.isFrozen}")
        }
    }
}