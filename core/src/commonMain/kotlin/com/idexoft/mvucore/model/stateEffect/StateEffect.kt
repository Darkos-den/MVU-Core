package com.idexoft.mvucore.model.stateEffect

import com.idexoft.mvucore.model.effect.Effect
import com.idexoft.mvucore.model.state.State

data class StateEffect(
    val state: State,
    val effects: List<Effect>
)