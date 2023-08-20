package com.example.room

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.example.data.MessageDataSource
import com.example.data.model.Message
import io.ktor.http.cio.websocket.*
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(
        username: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if (members.containsKey(username)) {
            throw MemberAlreadyExistException()
        }
        members[username] = Member(
            username = username,
            session = sessionId,
            socket = socket
        )
    }

    suspend fun sendMessage(sendUsername: String, message: String) {
        members.values.forEach { member ->
            val messageEntity = Message(
                text = message,
                username = sendUsername,
                timestamp = System.currentTimeMillis()
            )
            messageDataSource.insertMessage(messageEntity)

            val parsedMessage = Json.encodeToString(messageEntity)
            member.socket.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun getAllMessages(): List<Message> {
        return messageDataSource.getAllMessage()
    }

    suspend fun tryDisconnect(username: String) {
        members[username]?.socket?.close()
        if (members.containsKey(username)) {
            members.remove(username)
        }
    }

}