package com.example.sanar

fun MessageEntity.toMessage(): Message {
    return Message(
        text = this.text,
        isUser = this.isUser
    )
}
