package com.idexoft.mvucore.proxy

import com.idexoft.mvucore.api.ILogger
import com.idexoft.mvucore.api.Reducer
import com.idexoft.mvucore.model.message.Message
import com.idexoft.mvucore.model.state.State
import com.idexoft.mvucore.model.stateEffect.StateEffect

class ReducerLoggingProxy(
    private val reducer: Reducer,
    private val logger: ILogger
) : Reducer {

    override fun reduce(message: Message, state: State): StateEffect? {
        logger.d(TAG, "[reduce] message: $message, state: $state")
        return reducer.reduce(message, state)
    }

    companion object {
        private const val TAG = "MVU.Reducer"
    }
}