package com.idexoft.mvucore.stateProvider

import com.idexoft.mvucore.model.state.State

class InitialStateProvider {

    private val providers = HashMap<String, Provider>()

    fun registerState(owner: String, provider: Provider) {
        providers[owner] = provider
    }

    fun get(owner: String): State {
        return providers[owner]?.invoke()
            ?: throw IllegalArgumentException("provider for owner [$owner] not registered!")
    }
}