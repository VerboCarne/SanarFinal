package com.example.sanar

import androidx.room.*

@Dao
interface ConversationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation)

    @Query("SELECT * FROM conversations ORDER BY lastInteraction DESC")
    suspend fun getAllConversations(): List<Conversation>

    @Query("SELECT * FROM conversations WHERE id = :conversationId")
    suspend fun getConversation(conversationId: String): Conversation?

    @Delete
    suspend fun deleteConversation(conversation: Conversation): Int
}













