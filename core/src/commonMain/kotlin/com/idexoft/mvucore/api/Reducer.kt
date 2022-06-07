package com.idexoft.mvucore.api

import com.idexoft.mvucore.model.message.Message
import com.idexoft.mvucore.model.state.State
import com.idexoft.mvucore.model.stateEffect.StateEffect

interface Reducer {
    fun reduce(message: Message, state: State): StateEffect?
}