package com.idexoft.mvucore.model.stateEffect

import com.idexoft.mvucore.model.effect.Effect
import com.idexoft.mvucore.model.state.State

class TypedStateEffect<T: State>(
    val state: T,
    val effects: List<Effect>
)