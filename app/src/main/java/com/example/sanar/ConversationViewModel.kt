package com.example.sanar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ConversationViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val conversationDao = db.conversationDao()
    private val messageDao = db.messageDao()

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    private var chat = generativeModel.startChat()
    private var firstResponseSent = false
    private var currentConversationId: String? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _conversations.value = conversationDao.getAllConversations()
        }
        startNewConversation()
    }

    fun sendPrompt(prompt: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val conversationId = currentConversationId ?: UUID.randomUUID().toString()
                val existingConversation = conversationDao.getConversation(conversationId)

                val conversation = existingConversation?.copy(
                    title = if (existingConversation.title == "Nueva conversación") prompt else existingConversation.title,
                    lastInteraction = System.currentTimeMillis()
                ) ?: Conversation(
                    id = conversationId,
                    title = prompt,
                    lastInteraction = System.currentTimeMillis()
                )

                conversationDao.insertConversation(conversation)
                currentConversationId = conversationId
                _conversations.value = conversationDao.getAllConversations()

                val userMessage = MessageEntity(
                    conversationId = conversationId,
                    text = prompt,
                    isUser = true,
                    timestamp = System.currentTimeMillis()
                )
                messageDao.insertMessage(userMessage)

                // Respuesta del modelo generativo con prompt personalizado
                val response = chat.sendMessage("""
                Eres un chatbot empático y cariñoso especializado en apoyo emocional. 
                Tu objetivo es ayudar a las personas a expresar y comprender sus emociones. 
                Sigue estas reglas:

                1. Si el usuario expresa sus sentimientos o estado emocional:
                   - Primero responde con empatía y comprensión.
                   - Luego incluye una cita relevante de un autor o poeta en el formato: 
                      *"La cita irá aquí, centrada y en cursiva."* 
                                              - [Autor], [Obra/Fuente]
                   - Termina con un mensaje breve que conecte la cita con su situación.

                2. Si es una conversación normal sobre emociones:
                   - Mantén un tono cálido y comprensivo.
                   - Haz preguntas que ayuden a explorar más sus sentimientos.
                   - Mantén el foco en el bienestar emocional.

                3. Si el tema se desvía de las emociones:
                   - Reconoce amablemente su pregunta.
                   - Redirecciona la conversación hacia lo emocional.
                   Ejemplo: "Entiendo tu interés por las recetas, pero ¿qué te parece si exploramos 
                   qué emociones o recuerdos te conectan con la cocina?"
                   
                4. Reglas generales:
                   - Mantén respuestas concisas (máximo 300 caracteres).
                   - Usa un tono cálido y acogedor.
                   - Siempre invita a seguir compartiendo.
                   - Nunca des consejos médicos o psicológicos profesionales.

                Usuario: $prompt
            """.trimIndent())

                val botResponse = response.text ?: getFallbackMessage()

                val botMessage = MessageEntity(
                    conversationId = conversationId,
                    text = botResponse,
                    isUser = false,
                    timestamp = System.currentTimeMillis()
                )
                messageDao.insertMessage(botMessage)

                val allMessages = messageDao.getMessagesByConversation(conversationId)
                _uiState.value = UiState.Success(allMessages.map { it.toMessage() })
                firstResponseSent = true

            } catch (e: Exception) {
                val errorMessages = (_uiState.value as? UiState.Success)?.messages ?: listOf()
                val newMessages = errorMessages + Message(prompt, isUser = true) + Message(getFallbackMessage(), isUser = false)
                _uiState.value = UiState.Success(newMessages)
            }
        }
    }

    fun loadConversation(conversationId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val messages = messageDao.getMessagesByConversation(conversationId)

                if (messages.isNotEmpty()) {
                    _uiState.value = UiState.Success(messages.map { it.toMessage() })
                    currentConversationId = conversationId
                    firstResponseSent = false
                } else {
                    _uiState.value = UiState.Error("La conversación seleccionada está vacía.")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al cargar la conversación: ${e.localizedMessage}")
            }
        }
    }

    fun startNewConversation() {
        chat = generativeModel.startChat()
        _uiState.value = UiState.Success(emptyList())

        val newConversationId = UUID.randomUUID().toString()
        val newConversation = Conversation(
            id = newConversationId,
            title = "Nueva conversación",
            lastInteraction = System.currentTimeMillis()
        )

        viewModelScope.launch(Dispatchers.IO) {
            conversationDao.insertConversation(newConversation)
            _conversations.value = conversationDao.getAllConversations()
        }

        val welcomeMessage = Message(
            text = "¡Hola! ¿Cómo te sientes hoy?",
            isUser = false
        )
        _uiState.value = UiState.Success(listOf(welcomeMessage))
        firstResponseSent = false
    }

    private fun getFallbackMessage(): String {
        val fallbackMessages = listOf(
            "Lo siento, no puedo responder en este momento, pero estoy aquí para escucharte.",
            "Entiendo que estás pasando por un momento difícil. ¿Te gustaría contarme más?",
            "A veces, solo hablar ayuda. Estoy aquí para ti.",
            "Puede que no tenga una respuesta ahora, pero tus sentimientos son importantes.",
            "Dime cómo te sientes y haremos esto juntos."
        )
        return fallbackMessages.random()
    }
}





