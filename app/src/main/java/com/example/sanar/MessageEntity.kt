package com.example.sanar

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val messageId: Long = 0,
    val conversationId: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long
)

