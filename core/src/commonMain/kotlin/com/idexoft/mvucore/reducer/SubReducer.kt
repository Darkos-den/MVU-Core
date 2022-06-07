package com.idexoft.mvucore.reducer

import com.idexoft.mvucore.model.message.Message
import com.idexoft.mvucore.model.state.State
import com.idexoft.mvucore.model.stateEffect.StateEffect

abstract class SubReducer<T : State> {
    open fun readyToProcess(state: T): Boolean = true
    abstract fun reduce(message: Message, state: T): StateEffect?

    fun checkAndReduce(message: Message, state: T): StateEffect? {
        return if (readyToProcess(state)) {
            reduce(message, state)
        } else null
    }
}