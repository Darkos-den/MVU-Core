package com.idexoft.mvucore.api

interface INavigationComponent {
	fun replace(destination: String, args: Map<String, Any>? = null)
	fun push(destination: String, args: Map<String, Any>? = null)
	fun pop()
	fun forcePop(destination: String)

	fun observeReplace(observer: (String, Map<String, Any>?)->Unit)
	fun observePush(observer: (String, Map<String, Any>?)->Unit)
	fun observePop(observer: ()->Unit)
	fun observeForcePop(observer: (String)->Unit)

	companion object {
		fun default() = object: INavigationComponent {
			private var replaceObserver: ((String, Map<String, Any>?) -> Unit)? = null
			private var pushObserver: ((String, Map<String, Any>?) -> Unit)? = null
			private var popObserver: (() -> Unit)? = null
			private var forcePopObserver: ((String) -> Unit)? = null

			override fun observeReplace(observer: (String, Map<String, Any>?) -> Unit) {
				replaceObserver = observer
			}

			override fun observePush(observer: (String, Map<String, Any>?) -> Unit) {
				pushObserver = observer
			}

			override fun observePop(observer: () -> Unit) {
				popObserver = observer
			}

			override fun replace(destination: String, args: Map<String, Any>?) {
				replaceObserver?.invoke(destination, args)
			}

			override fun push(destination: String, args: Map<String, Any>?) {
				pushObserver?.invoke(destination, args)
			}

			override fun pop() {
				popObserver?.invoke()
			}

			override fun forcePop(destination: String) {
				forcePopObserver?.invoke(destination)
			}

			override fun observeForcePop(observer: (String) -> Unit) {
				forcePopObserver = observer
			}
		}
	}
}