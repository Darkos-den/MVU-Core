package com.idexoft.mvucore

import com.idexoft.mvucore.api.INavigationComponent

typealias ReplaceObserver = ((String, Map<String, Any>?) -> Unit)
typealias PushObserver = ((String, Map<String, Any>?) -> Unit)
typealias ForcePopObserver = ((String) -> Unit)
typealias PopObserver = ()->Unit

class LamdaContainer<T> {
    var lamda: T? = null
}

class DefaultNavigationComponent: INavigationComponent {
    private val replaceObserver = LamdaContainer<ReplaceObserver>()
    private val pushObserver = LamdaContainer<PushObserver>()
    private val popObserver = LamdaContainer<PopObserver>()
    private val forcePopObserver = LamdaContainer<ForcePopObserver>()

    override fun observeReplace(observer: (String, Map<String, Any>?) -> Unit) {
        replaceObserver.lamda = observer
    }

    override fun observePush(observer: (String, Map<String, Any>?) -> Unit) {
        pushObserver.lamda = observer
    }

    override fun observePop(observer: () -> Unit) {
        popObserver.lamda = observer
    }

    override fun replace(destination: String, args: Map<String, Any>?) {
        replaceObserver.lamda?.invoke(destination, args)
    }

    override fun push(destination: String, args: Map<String, Any>?) {
        pushObserver.lamda?.invoke(destination, args)
    }

    override fun pop() {
        popObserver.lamda?.invoke()
    }

    override fun forcePop(destination: String) {
        forcePopObserver.lamda?.invoke(destination)
    }

    override fun observeForcePop(observer: (String) -> Unit) {
        forcePopObserver.lamda = observer
    }
}