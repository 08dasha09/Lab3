package com.example.lab3.data.repository

import com.example.lab3.data.local.RecipeDao
import com.example.lab3.data.mapper.toDomain
import com.example.lab3.data.mapper.toEntity
import com.example.lab3.domain.models.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecipeRepository(
    private val recipeDao: RecipeDao
) {
    val recipes: Flow<List<Recipe>> =
        recipeDao.getAllRecipes().map { list -> list.map { it.toDomain() } }

    fun getRecipeById(id: Int): Flow<Recipe?> =
        recipeDao.getRecipeById(id).map { it?.toDomain() }

    suspend fun addRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe.toEntity())
    }

    suspend fun updateRecipe(recipe: Recipe) {
        recipeDao.updateRecipe(recipe.toEntity())
    }

    suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe.toEntity())
    }
}