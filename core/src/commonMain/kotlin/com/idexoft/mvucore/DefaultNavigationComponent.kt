package com.idexoft.mvucore

import co.touchlab.stately.isolate.IsolateState
import com.idexoft.mvucore.api.INavigationComponent

typealias ReplaceObserver = ((String, Map<String, Any>?) -> Unit)
typealias PushObserver = ((String, Map<String, Any>?) -> Unit)
typealias ForcePopObserver = ((String) -> Unit)
typealias PopObserver = ()->Unit

class LamdaContainer<T> {
    var lamda: T? = null
}

class DefaultNavigationComponent: INavigationComponent {
    private val replaceObserver = IsolateState { LamdaContainer<ReplaceObserver>() }
    private val pushObserver = IsolateState { LamdaContainer<PushObserver>() }
    private val popObserver = IsolateState { LamdaContainer<PopObserver>() }
    private val forcePopObserver = IsolateState { LamdaContainer<ForcePopObserver>() }

    override fun observeReplace(observer: (String, Map<String, Any>?) -> Unit) {
        replaceObserver.access { it.lamda = observer }
    }

    override fun observePush(observer: (String, Map<String, Any>?) -> Unit) {
        pushObserver.access { it.lamda = observer }
    }

    override fun observePop(observer: () -> Unit) {
        popObserver.access { it.lamda = observer }
    }

    override fun replace(destination: String, args: Map<String, Any>?) {
        replaceObserver.access { it.lamda?.invoke(destination, args) }
    }

    override fun push(destination: String, args: Map<String, Any>?) {
        pushObserver.access { it.lamda?.invoke(destination, args) }
    }

    override fun pop() {
        popObserver.access { it.lamda?.invoke() }
    }

    override fun forcePop(destination: String) {
        forcePopObserver.access { it.lamda?.invoke(destination) }
    }

    override fun observeForcePop(observer: (String) -> Unit) {
        forcePopObserver.access { it.lamda = observer }
    }
}