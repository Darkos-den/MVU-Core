package com.idexoft.mvucore.model.message

import com.idexoft.mvucore.model.state.StateModel

abstract class TypedMessage<T: StateModel>: Message()