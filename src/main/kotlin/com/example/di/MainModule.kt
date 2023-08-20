package com.example.di

import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import com.example.data.MessageDataSource
import com.example.data.MessageDataSourceImpl
import com.example.room.RoomController

val mainModule = module {

    single<CoroutineDatabase> {
        KMongo.createClient().coroutine.getDatabase("message_db")
    }

    single<MessageDataSource> {
        MessageDataSourceImpl(db = get())
    }

    single {
        RoomController(messageDataSource = get())
    }

}