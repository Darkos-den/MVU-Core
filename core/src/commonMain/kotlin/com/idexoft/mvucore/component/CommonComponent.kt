package com.idexoft.mvucore.component

import co.touchlab.stately.isFrozen
import co.touchlab.stately.isolate.IsolateState
import com.idexoft.mvucore.messageParser.MessageParser
import com.idexoft.mvucore.api.*
import com.idexoft.mvucore.component.state.Active
import com.idexoft.mvucore.component.state.ComponentState
import com.idexoft.mvucore.component.state.Destroyed
import com.idexoft.mvucore.model.effect.None
import com.idexoft.mvucore.model.message.Idle
import com.idexoft.mvucore.model.message.LifecycleMessage
import com.idexoft.mvucore.model.message.Message
import com.idexoft.mvucore.model.state.State
import com.idexoft.mvucore.utils.Logger
import kotlinx.coroutines.*

typealias StateListener = (Long, State) -> Unit

class CommonComponent(
    private val store: IStateStore,
    private val reducer: Reducer,
    private val messageParser: MessageParser,
    private val effectHandler: EffectHandler,
    override val navigationComponent: INavigationComponent
) : IComponent {

    init {
        Logger.d("SKA", "====| init component")
    }

//    private val ehContainer = IsolateState { Container<EffectHandler>() }

    init {
        Logger.d("SKA", "====| this: ${this.isFrozen}")
        Logger.d("SKA", "====| reducer: ${reducer.isFrozen}")
        Logger.d("SKA", "====| store: ${store.isFrozen}")
        Logger.d("SKA", "====| eh: ${effectHandler.isFrozen}")
        Logger.d("SKA", "====| nav: ${navigationComponent.isFrozen}")
//        ehContainer.access {
//            it.value = effectHandler
//        }
    }

    internal var stateListener: StateListener? = null
//    private var state: ComponentState = Active(
//        store = store,
//        reducer = reducer,
//        effectHandler = effectHandler,
//        context = this
//    )

//    private val processor = Processor(
//        store = store,
//        reducer = reducer,
//        messageParser = messageParser,
//        effectHandler = ehContainer
//    )

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
//        state = Destroyed()
//        store.clear()
//        stateListener = null
    }

    internal fun onMessageReceived(
        owner: String,
        id: Long,
        message: Message
    ) {
        Logger.d("SKA", "on message received")
        Logger.d("SKA", "----| 00 stateStore: ${store.isFrozen}")
        if(globalReducer == null) {
            globalReducer = reducer
            globalStore = store
            globalStateListener = stateListener
        }

        if (message == LifecycleMessage.Clear) {
            store.remove(id)
            return
        }

        Logger.d("SKA", "----| 1 stateStore: ${globalStore.isFrozen}")
        Logger.d("SKA", "----| 0 stateStore: ${store.isFrozen}")

        val state: State = globalStore!!.getOrCreate(id, owner)

        messageProcessing(id, message, state, effectHandler)
    }
}