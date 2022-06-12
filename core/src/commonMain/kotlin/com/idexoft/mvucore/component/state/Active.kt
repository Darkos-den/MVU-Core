package com.idexoft.mvucore.component.state

import com.idexoft.mvucore.api.EffectHandler
import com.idexoft.mvucore.api.IStateStore
import com.idexoft.mvucore.api.Reducer
import com.idexoft.mvucore.component.CommonComponent
import com.idexoft.mvucore.model.*
import com.idexoft.mvucore.model.effect.*
import com.idexoft.mvucore.model.message.Idle
import com.idexoft.mvucore.model.message.LifecycleMessage
import com.idexoft.mvucore.model.message.Message
import com.idexoft.mvucore.model.state.State
import com.idexoft.mvucore.model.stateEffect.StateEffect
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class Active(
    private val store: IStateStore,
    private val reducer: Reducer,
    private val context: CommonComponent,
    private val effectHandler: EffectHandler
) : ComponentState {

    override fun onMessageReceived(owner: String, id: Long, message: Message) {
        if (message == Idle) {
            return
        }

        if (message == LifecycleMessage.Clear) {
            store.remove(id)
            return
        }

        var state: State? = store.getOrCreate(id, owner)

        processMessage(
            message = message,
            id = id,
            state = state!!
        ).takeIf {
            !it && message !is LifecycleMessage
        }?.let {
//            do {
//                val parentId = state?.parentId ?: throw IllegalArgumentException("reducer for message [${message}] not registered!")
//
//                val result = store.getOrNull(parentId)?.let {
//                    state = it
//                    processMessage(
//                        message = message,
//                        id = parentId,
//                        state = it
//                    )
//                }?.let {
//                    !it
//                } ?: false
//            } while (result)
        }
    }

    /**
     * @return is message processed:
     *  - true if message processed
     *  - false if message not processed
     */
    private fun processMessage(
        message: Message,
        id: Long,
        state: State
    ): Boolean {
        return reducer.reduce(
            message = message,
            state = state
        )?.also {
            updateState(
                id = id,
                state = it.state,
                stateEffect = it
            )
        }?.effects?.forEach { effect ->
            val owner = state::class.simpleName!!.let {
                if(it.contains("State")){
                    it.substring(0, it.indexOf("State"))
                } else {
                    it
                }
            }
            processEffect(effect, id, owner)
        }?.let {
            true
        } ?: run {
            false
        }
    }

    private fun updateState(
        id: Long,
        state: State,
        stateEffect: StateEffect,
    ) {
        store.updateState(id, stateEffect.state)
        context.stateListener?.invoke(id, state)
    }

    private fun processEffect(
        effect: Effect,
        id: Long,
        owner: String
    ) {
        if (effect == None) {
            return
        }
        if (effect is SingleEffect || effect is FlowEffect) {
            store.cancelNamedJob(id, effect.effectName())
        }
        registerJob(
            effect = effect,
            id = id,
            job = runJob(effect, owner, id)
        )
    }

    private fun registerJob(
        effect: Effect,
        id: Long,
        job: Job
    ) {
        if (effect is SingleEffect || effect is FlowEffect) {
            store.registerNamedJob(
                id = id,
                name = effect.effectName(),
                job = job
            )
        } else {
            store.registerJob(id, job)
        }
    }

    private fun runJob(
        effect: Effect,
        owner: String,
        id: Long
    ): Job = CoroutineScope(Dispatchers.Default).launch {
        if (effect is FlowEffect) {
            try {
                effectHandler.executeFlow(effect)!!.collect {
                    processMessage(owner, id, it)
                }
            } catch (e: Exception) {
                //do nothing
            }
        } else {
            processMessage(owner, id, effectHandler.execute(effect)!!)
        }
    }

    private suspend fun processMessage(
        owner: String,
        id: Long,
        it: Message
    ) {
        withContext(Dispatchers.Main) {
            context.onMessageReceived(
                owner = owner,
                id = id,
                message = it
            )
        }
    }
}