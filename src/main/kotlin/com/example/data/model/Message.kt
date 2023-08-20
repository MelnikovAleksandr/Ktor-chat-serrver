package com.example.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@kotlinx.serialization.Serializable
data class Message(
    @BsonId
    val id: String = ObjectId().toString(),
    val text: String,
    val username: String,
    val timestamp: Long
)
