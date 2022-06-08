package com.idexoft.mvucore.proxy

import com.idexoft.mvucore.api.ILogger
import com.idexoft.mvucore.api.IStateStore
import com.idexoft.mvucore.model.state.State
import kotlinx.coroutines.Job

class StateStoreLoggingProxy(
    private val stateStore: IStateStore,
    private val logger: ILogger
) : IStateStore {

    override val activeStates: List<Pair<Long, State>>
        get() = stateStore.activeStates

    override fun getOrCreate(id: Long, owner: String): State {
        return stateStore.getOrCreate(id, owner)
    }

    override fun getOrNull(id: Long): State? {
        return stateStore.getOrNull(id)
    }

    override fun create(id: Long, state: State) {
        stateStore.create(id, state)
        logActiveStates("create")
    }

    override fun updateState(id: Long, state: State) {
        stateStore.updateState(id, state)
    }

    override fun registerJob(id: Long, job: Job) {
        stateStore.registerJob(id, job)
    }

    override fun registerNamedJob(id: Long, name: String, job: Job) {
        stateStore.registerNamedJob(id, name, job)
    }

    override fun cancelNamedJob(id: Long, name: String) {
        stateStore.cancelNamedJob(id, name)
    }

    override fun remove(id: Long) {
        stateStore.remove(id)
        logActiveStates("remove")
    }

    override fun clear() {
        stateStore.clear()
        logActiveStates("clear")
    }

    private fun logActiveStates(from: String) {
        logger.d(
            TAG,
            "[$from] active states: ${activeStates.map { "{id: ${it.first}, state: ${it.second}}" }}"
        )
    }

    companion object {
        private const val TAG = "MVU.StateStore"
    }
}