package com.example.lab3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lab3.data.repository.RecipeRepository
import com.example.lab3.domain.models.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    val recipes = repository.recipes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun getRecipeById(id: Int): Flow<Recipe?> = repository.getRecipeById(id)

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.addRecipe(recipe)
        }
    }

    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.updateRecipe(recipe)
        }
    }

    fun addOrUpdateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            if (recipe.id == 0) {
                repository.addRecipe(recipe)
            } else {
                repository.updateRecipe(recipe)
            }
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.deleteRecipe(recipe)
        }
    }
}

class RecipeViewModelFactory(
    private val repository: RecipeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}