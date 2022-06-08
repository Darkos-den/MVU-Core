package com.idexoft.mvucore.proxy

import com.idexoft.mvucore.component.StateListener
import com.idexoft.mvucore.api.IComponent
import com.idexoft.mvucore.api.ILogger
import com.idexoft.mvucore.api.INavigationComponent

class ComponentLoggingProxy(
    private val component: IComponent,
    private val logger: ILogger
) : IComponent {

    override val navigationComponent: INavigationComponent
        get() = component.navigationComponent

    override fun onMessageReceived(
        owner: String,
        id: Long,
        params: HashMap<String, Any>,
        method: String
    ) {
        component.onMessageReceived(owner, id, params, method)
    }

    override fun observeStateChanges(block: StateListener) {
        logger.d(
            TAG,
            "================================ start session ================================"
        )
        component.observeStateChanges { id, state ->
            logger.d(TAG, "[observeStateChanges] id: $id, state: $state")
            block(id, state)
        }
    }

    override fun onDestroy() {
        component.onDestroy()
        logger.d(
            TAG,
            "================================ close session ================================"
        )
    }

    companion object {
        private const val TAG = "MVU.Component"
    }
}