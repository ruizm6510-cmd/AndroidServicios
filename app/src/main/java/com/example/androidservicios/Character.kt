package com.example.androidservicios

import java.io.Serializable

data class Character(
    val id: Int,
    val name: String,
    val species: String,
    val status: String,
    val origin: String,
    val imageUrl: String
) : Serializable
