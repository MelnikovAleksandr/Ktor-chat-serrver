package com.example.routes

import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import com.example.room.MemberAlreadyExistException
import com.example.room.RoomController
import io.ktor.http.cio.websocket.*
import com.example.session.ChatSession

fun Route.chatSocket(roomController: RoomController) {

    webSocket("/chat-socket") {
        val session = call.sessions.get<ChatSession>()
        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
            return@webSocket
        }
        try {
            roomController.onJoin(
                username = session.username,
                sessionId = session.sessionId,
                socket = this
            )
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    roomController.sendMessage(
                        sendUsername = session.username,
                        message = frame.readText()
                    )
                }
            }
        } catch (e: MemberAlreadyExistException) {
            call.respond(HttpStatusCode.Conflict)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            roomController.tryDisconnect(session.username)
        }
    }
}

fun Route.getAllMessages(roomController: RoomController) {
    get("/messages") {
        call.respond(HttpStatusCode.OK, roomController.getAllMessages())
    }
}



















