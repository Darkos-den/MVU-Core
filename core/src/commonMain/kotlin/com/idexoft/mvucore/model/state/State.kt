package com.idexoft.mvucore.model.state

abstract class State {
    var parentId: Long? = null

    abstract fun toMap(): Map<String, Any?>
}