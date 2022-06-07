package com.idexoft.mvucore.reducer

import com.idexoft.mvucore.api.IScopedReducer
import com.idexoft.mvucore.api.Reducer
import com.idexoft.mvucore.model.effect.Effect
import com.idexoft.mvucore.model.effect.None
import com.idexoft.mvucore.model.message.Idle
import com.idexoft.mvucore.model.message.Message
import com.idexoft.mvucore.model.state.State
import com.idexoft.mvucore.model.stateEffect.StateEffect
import kotlin.reflect.KClass
import kotlin.reflect.cast

abstract class TypedReducer<T: State>: Reducer {

    private var subReducers: List<SubReducer<T>> = emptyList()

    fun registerSubReducer(subReducer: SubReducer<T>) {
        subReducers = subReducers + subReducer
    }

    abstract fun getStateClass(): KClass<T>

    override fun reduce(message: Message, state: State): StateEffect? {
        if (message is Idle) {
            return StateEffect(
                state = state,
                effect = None
            )
        }
        return state.takeIf {
            getStateClass().isInstance(state)
        }?.let {
            var tempState = state
            var processed = false
            var effects = emptyList<Effect>()

            var scopes = emptyList<String>()

            subReducers.forEach { subReducer ->
                subReducer.checkAndReduce(
                    message,
                    getStateClass().cast(tempState)
                )?.let {
                    if (subReducer is IScopedReducer) {
                        if (scopes.contains(subReducer.scope)) {
                            return@forEach
                        } else {
                            scopes = scopes + subReducer.scope
                        }
                    }

                    processed = true
                    tempState = it.state
                    effects = effects + it.effects
                }
            }
            reducer(
                message,
                getStateClass().cast(tempState)
            )?.let {
                processed = true
                tempState = it.state
                effects = effects + it.effects
            }

            if (processed) {
                return StateEffect(
                    state = tempState,
                    effects = effects
                )
            } else {
                return null
            }
        }
    }

    protected abstract fun reducer(message: Message, state: T): StateEffect?
}