package com.idexoft.mvucore

import com.idexoft.mvucore.api.Reducer
import com.idexoft.mvucore.model.*
import com.idexoft.mvucore.model.message.Message
import com.idexoft.mvucore.model.state.State
import com.idexoft.mvucore.model.stateEffect.StateEffect
import kotlin.reflect.KClass

class MainReducer : Reducer {

    private val reducers = HashMap<String, Reducer>()

    fun registerReducer(stateClass: KClass<out State>, reducer: Reducer) {
        reducers[stateClass.qualifiedName!!] = reducer
    }

    override fun reduce(message: Message, state: State): StateEffect? {
        return reducers[state::class.qualifiedName]?.reduce(message, state)
    }
}