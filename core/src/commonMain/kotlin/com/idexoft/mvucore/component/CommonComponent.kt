package com.idexoft.mvucore.component

import com.idexoft.mvucore.messageParser.MessageParser
import com.idexoft.mvucore.api.*
import com.idexoft.mvucore.component.state.Active
import com.idexoft.mvucore.component.state.ComponentState
import com.idexoft.mvucore.component.state.Destroyed
import com.idexoft.mvucore.model.message.Message
import com.idexoft.mvucore.model.state.State

typealias StateListener = (Long, State) -> Unit

class CommonComponent(
    private val store: IStateStore,
    reducer: Reducer,
    private val messageParser: MessageParser,
    effectHandler: EffectHandler,
    override val navigationComponent: INavigationComponent
) : IComponent {

    internal var stateListener: StateListener? = null
    private var state: ComponentState = Active(
        store = store,
        reducer = reducer,
        effectHandler = effectHandler,
        context = this
    )

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
        state = Destroyed()
        store.clear()
        stateListener = null
    }

    internal fun onMessageReceived(
        owner: String,
        id: Long,
        message: Message
    ) {
        state.onMessageReceived(owner, id, message)
    }
}