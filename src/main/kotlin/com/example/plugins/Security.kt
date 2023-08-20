package com.example.plugins

import io.ktor.application.*
import io.ktor.sessions.*
import com.example.session.ChatSession

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<ChatSession>("MY_SESSION")
    }

    intercept(ApplicationCallPipeline.Features) {
        if (call.sessions.get<ChatSession>() == null) {
            val username = call.parameters["username"] ?: "Guest"
            call.sessions.set(ChatSession(username, generateSessionId()))
        }
    }
}
