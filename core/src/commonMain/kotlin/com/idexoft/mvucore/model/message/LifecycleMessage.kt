package com.idexoft.mvucore.model.message

sealed class LifecycleMessage : Message() {
    object Init : LifecycleMessage()
    object Clear : LifecycleMessage()
}