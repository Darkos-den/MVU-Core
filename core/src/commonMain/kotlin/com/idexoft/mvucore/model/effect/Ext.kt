package com.idexoft.mvucore.model.effect

fun Effect.effectName(): String = this::class.simpleName.orEmpty()