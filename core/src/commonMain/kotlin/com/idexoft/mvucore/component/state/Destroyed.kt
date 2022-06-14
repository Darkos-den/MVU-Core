package com.idexoft.mvucore.component.state

import com.idexoft.mvucore.model.message.Message

class Destroyed : ComponentState {

    override fun onMessageReceived(owner: String, id: Long, message: Message) {
        throw IllegalStateException("component is destroyed")
    }
}