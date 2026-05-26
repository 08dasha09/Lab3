package com.example.lab3.data.mapper

import com.example.lab3.data.local.RecipeEntity
import com.example.lab3.domain.models.Recipe

fun RecipeEntity.toDomain(): Recipe = Recipe(
    id = id,
    name = name,
    ingredients = ingredients,
    instructions = instructions,
    timeMin = timeMin,
    difficulty = difficulty
)

fun Recipe.toEntity(): RecipeEntity = RecipeEntity(
    id = id,
    name = name,
    ingredients = ingredients,
    instructions = instructions,
    timeMin = timeMin,
    difficulty = difficulty
)