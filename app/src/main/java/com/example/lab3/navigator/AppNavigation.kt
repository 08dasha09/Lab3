package com.example.lab3.navigator

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab3.data.local.DatabaseProvider
import com.example.lab3.data.repository.RecipeRepository
import com.example.lab3.screen.AddRecipeScreen
import com.example.lab3.screen.DetailsRecipeScreen
import com.example.lab3.screen.RecipesListScreen
import com.example.lab3.ui.viewmodel.RecipeViewModel
import com.example.lab3.ui.viewmodel.RecipeViewModelFactory

@Composable
fun RecipeApp() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val repository = RecipeRepository(database.recipeDao())

    val viewModel: RecipeViewModel = viewModel(
        factory = RecipeViewModelFactory(repository)
    )

    NavHost(navController = navController, startDestination = "list") {

        composable("list") {
            RecipesListScreen(navController, viewModel)
        }

        composable("add/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: "new"
            AddRecipeScreen(navController, viewModel, recipeId)
        }

        composable("details/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
            if (recipeId != null) {
                DetailsRecipeScreen(navController, viewModel, recipeId)
            }
        }
    }
}