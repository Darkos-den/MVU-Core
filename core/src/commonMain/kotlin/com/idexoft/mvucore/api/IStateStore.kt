package com.idexoft.mvucore.api

import com.idexoft.mvucore.model.state.State
import kotlinx.coroutines.Job

interface IStateStore {
    val activeStates: List<Pair<Long, State>>

    fun getOrCreate(id: Long, owner: String): State
    fun getOrNull(id: Long): State?
    fun create(id: Long, state: State)
    fun updateState(id: Long, state: State)
    fun registerJob(id: Long, job: Job)
    fun registerNamedJob(id: Long, name: String, job: Job)
    fun cancelNamedJob(id: Long, name: String)
    fun remove(id: Long)

    fun clear()
}