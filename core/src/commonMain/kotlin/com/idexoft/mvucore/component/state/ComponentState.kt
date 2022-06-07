package com.idexoft.mvucore.component.state

import com.idexoft.mvucore.model.message.Message

interface ComponentState {

    fun onMessageReceived(
        owner: String,
        id: Long,
        message: Message
    )
}