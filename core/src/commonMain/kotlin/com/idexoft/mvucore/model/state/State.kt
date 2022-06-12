package com.idexoft.mvucore.model.state

import co.touchlab.stately.isolate.IsolateState

abstract class State {
    val parentIdState = IsolateState { ParentIdState() }

    abstract fun toMap(): Map<String, Any?>
}