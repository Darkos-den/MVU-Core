package com.idexoft.mvucore.model.stateEffect

import com.idexoft.mvucore.model.effect.Effect
import com.idexoft.mvucore.model.effect.None
import com.idexoft.mvucore.model.state.State

@StateEffectDsl
fun StateEffect(state: State, effect: Effect = None): StateEffect {
    return StateEffect(
        state = state,
        effects = listOf(effect)
    )
}

@StateEffectDsl
fun <T: State> TypedStateEffect(state: T, effect: Effect = None): TypedStateEffect<T> {
    return TypedStateEffect(
        state = state,
        effects = listOf(effect)
    )
}

@StateEffectDsl
fun StateEffect(state: State, vararg effect: Effect): StateEffect {
    return StateEffect(
        state = state,
        effects = effect.toList()
    )
}