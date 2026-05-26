package com.example.lab3.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val ingredients: String,
    val instructions: String,
    val timeMin: Int,
    val difficulty: String
)