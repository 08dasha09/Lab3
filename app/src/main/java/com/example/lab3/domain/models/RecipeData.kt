package com.example.lab3.domain.models

data class Recipe(
    val id: Int = 0,
    val name: String,
    val ingredients: String,
    val instructions: String,
    val timeMin: Int,
    val difficulty: String
)