package com.example.room

class MemberAlreadyExistException : Exception(
    "There are is already a member with same name in the room, change the username"
)