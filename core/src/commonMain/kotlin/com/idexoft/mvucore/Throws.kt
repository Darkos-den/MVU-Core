package com.idexoft.mvucore

import com.idexoft.mvucore.model.effect.Effect
import com.idexoft.mvucore.model.message.Message

fun flowNotSupported(): Nothing {
    throw IllegalStateException("FlowEffect not supported")
}

fun notSupportedEffect(effect: Effect): Nothing {
    throw IllegalArgumentException("effect ${effect::class.simpleName} not supported")
}

fun handleInvalidEffect(effect: Effect): Nothing {
    throw IllegalArgumentException("invalid effect: $effect")
}

fun handleInvalidMessage(message: Message): Nothing {
    throw IllegalArgumentException("invalid message: $message")
}