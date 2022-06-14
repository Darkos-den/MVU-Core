package com.idexoft.mvucore.model.state

abstract class State {
    val parentIdState = ParentIdState()

    abstract fun toMap(): Map<String, Any?>
}