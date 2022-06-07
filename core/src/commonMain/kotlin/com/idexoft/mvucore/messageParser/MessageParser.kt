package com.idexoft.mvucore.messageParser

import com.idexoft.mvucore.model.message.Message

class MessageParser {

    private val parsers = HashMap<String, Parser>()

    fun registerParser(message: String, parser: Parser) {
        parsers[message] = parser
    }

    fun parse(message: String, params: HashMap<String, Any>): Message {
        return parsers[message]?.invoke(params)
            ?: throw IllegalArgumentException("parser for message [$message] not registered!")
    }
}