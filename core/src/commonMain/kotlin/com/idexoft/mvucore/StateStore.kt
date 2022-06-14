package com.idexoft.mvucore

import com.idexoft.mvucore.api.IStateStore
import com.idexoft.mvucore.model.state.ParentState
import com.idexoft.mvucore.model.state.State
import com.idexoft.mvucore.stateProvider.InitialStateProvider
import kotlinx.coroutines.Job
import kotlin.jvm.Synchronized

class StateStore(
    private val provider: InitialStateProvider
) : IStateStore {
    private val states = HashMap<Long, State>()
    private val jobs = HashMap<Long, List<Job>>()
    private val namedJobs = HashMap<Long, HashMap<String, Job>>()

    private var lastParentId: Long? = null

    override val activeStates: List<Pair<Long, State>>
        get() {
            return states.toList()
        }

    override fun getOrCreate(id: Long, owner: String): State {
        return states[id] ?: provider.get(owner).also {
            create(id, it)
        }.also {
            val copy = lastParentId
            it.parentIdState.id = copy
            if(it is ParentState){
                lastParentId = id
            }
        }
    }

    override fun getOrNull(id: Long): State? {
        return states[id]
    }

    override fun create(id: Long, state: State) {
        states[id] = state
    }

    override fun updateState(id: Long, state: State) {
        val parentId = states[id]?.parentIdState?.id
        state.parentIdState.id = parentId
        states[id] = state
    }

    override fun registerJob(id: Long, job: Job) {
        jobs[id] = (jobs[id] ?: emptyList()) + job
    }

    override fun registerNamedJob(id: Long, name: String, job: Job) {
        namedJobs[id] = (namedJobs[id] ?: HashMap()).apply {
            this[name] = job
        }
    }

    override fun cancelNamedJob(id: Long, name: String) {
        namedJobs[id]?.get(name)?.cancel()
        namedJobs[id]?.remove(name)
    }

    override fun remove(id: Long) {
        states.remove(id)
        jobs[id]?.forEach {
            it.cancel()
        }
        jobs.remove(id)
        namedJobs[id]?.values?.forEach {
            it.cancel()
        }
        namedJobs.remove(id)
    }

    override fun clear() {
        jobs.map { it.value }.flatten().forEach {
            it.cancel()
        }
        jobs.clear()
        namedJobs.map { it.value.values }.flatten().forEach {
            it.cancel()
        }
        namedJobs.clear()
        states.clear()
    }
}