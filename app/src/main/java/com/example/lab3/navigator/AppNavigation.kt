package com.example.lab3.navigator

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab3.models.RecipeViewModel
import com.example.lab3.screen.AddRecipeScreen
import com.example.lab3.screen.DetailsRecipeScreen
import com.example.lab3.screen.RecipesListScreen

@Composable
fun RecipeApp() {
    val navController = rememberNavController()
    val viewModel: RecipeViewModel = viewModel()

    NavHost(navController = navController, startDestination = "list") {

        composable("list") {
            RecipesListScreen(navController, viewModel)
        }


        composable("add/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")
            AddRecipeScreen(navController, viewModel, recipeId)
        }

        composable("details/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")
            if (recipeId != null) {
                DetailsRecipeScreen(navController, viewModel, recipeId)
            }
        }
    }
}