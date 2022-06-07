package com.idexoft.mvucore.model.stateEffect

import com.idexoft.mvucore.model.effect.Effect
import com.idexoft.mvucore.model.effect.None
import com.idexoft.mvucore.model.state.State

fun StateEffect.addEffect(effect: Effect): StateEffect {
    return this.copy(
        effects = effects + effect
    )
}

fun StateEffect.addEffect(stateEffect: StateEffect): StateEffect {
    return this.copy(
        effects = effects + stateEffect.effects
    )
}

fun State.makeEmptyStateEffect() = StateEffect(
    state = this,
    effect = None
)