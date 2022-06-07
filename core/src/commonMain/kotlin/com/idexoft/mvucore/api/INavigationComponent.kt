package com.idexoft.mvucore.api

interface INavigationComponent {
	fun replace(destination: String, args: Map<String, Any>? = null)
	fun push(destination: String, args: Map<String, Any>? = null)
	fun pop()

	fun observeReplace(observer: (String, Map<String, Any>?)->Unit)
	fun observePush(observer: (String, Map<String, Any>?)->Unit)
	fun observePop(observer: ()->Unit)

	companion object {
		fun empty() = object: INavigationComponent {
			override fun replace(destination: String, args: Map<String, Any>?) {
			}

			override fun push(destination: String, args: Map<String, Any>?) {
			}

			override fun pop() {
			}

			override fun observeReplace(observer: (String, Map<String, Any>?) -> Unit) {
			}

			override fun observePush(observer: (String, Map<String, Any>?) -> Unit) {
			}

			override fun observePop(observer: () -> Unit) {
			}
		}
	}
}