package com.idexoft.mvucore.api

import com.idexoft.mvucore.component.StateListener

interface IComponent {
    val navigationComponent: INavigationComponent

    fun onMessageReceived(
        owner: String,
        id: Long,
        params: HashMap<String, Any>,
        method: String
    )

    fun observeStateChanges(block: StateListener)
    fun onDestroy()
}