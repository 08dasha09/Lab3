package com.example.lab3.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val ingredients: String,
    val instructions: String,
    val timeMin: Int,
    val difficulty: String
)

class RecipeViewModel : ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    init {
        _recipes.value = listOf(
            Recipe(
                name = "Панетоне",
                ingredients = "Борошно Caputo Manitoba Oro, закваска, жовтки, масло...",
                instructions = "Довгий заміс, ферментація 12 годин...",
                timeMin = 720,
                difficulty = "Складно"
            ),
            Recipe(
                name = "Курка з розмарином",
                ingredients = "Курка, свіжий розмарин, часник, оливкова олія",
                instructions = "Замаринувати і запікати до золотистої скоринки.",
                timeMin = 60,
                difficulty = "Легко"
            )
        )
    }

    fun addOrUpdateRecipe(recipe: Recipe) {
        val currentList = _recipes.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == recipe.id }
        if (index != -1) {
            currentList[index] = recipe
        } else {
            currentList.add(recipe)
        }
        _recipes.value = currentList
    }

    fun deleteRecipe(id: String) {
        _recipes.value = _recipes.value.filter { it.id != id }
    }

    fun getRecipeById(id: String): Recipe? {
        return _recipes.value.find { it.id == id }
    }
}