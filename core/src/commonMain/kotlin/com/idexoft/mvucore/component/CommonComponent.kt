package com.idexoft.mvucore.component

import com.idexoft.mvucore.messageParser.MessageParser
import com.idexoft.mvucore.api.*
import com.idexoft.mvucore.model.message.LifecycleMessage
import com.idexoft.mvucore.model.message.Message
import com.idexoft.mvucore.model.state.State

typealias StateListener = (Long, State) -> Unit

class CommonComponent(
    private val store: IStateStore,
    private val reducer: Reducer,
    private val messageParser: MessageParser,
    private val effectHandler: EffectHandler,
    override val navigationComponent: INavigationComponent
) : IComponent {

    private var stateListener: StateListener? = null

    override fun onMessageReceived(
        owner: String,
        id: Long,
        params: HashMap<String, Any>,
        method: String
    ) {
        onMessageReceived(owner, id, messageParser.parse(method, params))
    }

    override fun observeStateChanges(block: StateListener) {
        stateListener = block
    }

    override fun onDestroy() {
        store.clear()
        stateListener = null
    }

    private fun onMessageReceived(
        owner: String,
        id: Long,
        message: Message
    ) {
        if(globalReducer == null) {
            globalReducer = reducer
            globalStore = store
            globalStateListener = stateListener
        }

        if (message == LifecycleMessage.Clear) {
            store.remove(id)
            return
        }

        val state: State = globalStore!!.getOrCreate(id, owner)

        messageProcessing(id, message, state, effectHandler)
    }
}