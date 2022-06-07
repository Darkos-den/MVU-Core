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
        @Synchronized get() {
            return states.toList()
        }

    @Synchronized
    override fun getOrCreate(id: Long, owner: String): State {
        return states[id] ?: provider.get(owner).also {
            create(id, it)
        }.also {
            it.parentId = lastParentId
            if(it is ParentState){
                lastParentId = id
            }
        }
    }

    @Synchronized
    override fun getOrNull(id: Long): State? {
        return states[id]
    }

    @Synchronized
    override fun create(id: Long, state: State) {
        states[id] = state
    }

    @Synchronized
    override fun updateState(id: Long, state: State) {
        state.parentId = states[id]?.parentId
        states[id] = state
    }

    @Synchronized
    override fun registerJob(id: Long, job: Job) {
        jobs[id] = (jobs[id] ?: emptyList()) + job
    }

    @Synchronized
    override fun registerNamedJob(id: Long, name: String, job: Job) {
        namedJobs[id] = (namedJobs[id] ?: HashMap()).apply {
            this[name] = job
        }
    }

    @Synchronized
    override fun cancelNamedJob(id: Long, name: String) {
        namedJobs[id]?.get(name)?.cancel()
        namedJobs[id]?.remove(name)
    }

    @Synchronized
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

    @Synchronized
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