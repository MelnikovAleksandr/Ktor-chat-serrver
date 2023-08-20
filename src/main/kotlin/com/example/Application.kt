package com.example

import com.example.plugins.*
import io.ktor.application.*
import org.koin.ktor.ext.Koin
import com.example.di.mainModule

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        modules(mainModule)
    }
    configureSockets()
    configureRouting()
    configureSerialization()
    configureMonitoring()
    configureSecurity()

}
