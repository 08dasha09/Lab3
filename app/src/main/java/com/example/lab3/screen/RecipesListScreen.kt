package com.example.lab3.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lab3.domain.models.Recipe
import com.example.lab3.ui.viewmodel.RecipeViewModel

val Burgundy = Color(0xFF430119)
val DustyRose = Color(0xFFE0BCD0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesListScreen(navController: NavController, viewModel: RecipeViewModel) {
    val recipes by viewModel.recipes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мої рецепти", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Burgundy)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add/new") },
                containerColor = Burgundy,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Додати")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(recipes) { recipe ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable {
                        navController.navigate("details/${recipe.id}")
                    },
                    colors = CardDefaults.cardColors(containerColor = DustyRose)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = recipe.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Burgundy)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "⏱ ${recipe.timeMin} хв", color = Color.DarkGray)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(navController: NavController, viewModel: RecipeViewModel, recipeId: String) {
    val isEditing = recipeId != "new"
    val editingId = recipeId.toIntOrNull()

    val existingRecipe by if (isEditing && editingId != null) {
        viewModel.getRecipeById(editingId).collectAsState(initial = null)
    } else {
        remember { mutableStateOf(null) }
    }

    var name by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var timeMin by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("Легко") }

    // Заповнюємо поля, коли дані рецепту завантажились із бази
    LaunchedEffect(existingRecipe) {
        existingRecipe?.let {
            name = it.name
            ingredients = it.ingredients
            instructions = it.instructions
            timeMin = it.timeMin.toString()
            difficulty = it.difficulty
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Редагувати рецепт" else "Новий рецепт", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Burgundy),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Назва рецепту") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = ingredients, onValueChange = { ingredients = it },
                label = { Text("Інгредієнти") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = instructions, onValueChange = { instructions = it },
                label = { Text("Інструкції") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = timeMin, onValueChange = { timeMin = it },
                label = { Text("Час приготування (хв)") }, modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) { Text("Скасувати") }

                Button(
                    onClick = {
                        val newRecipe = Recipe(
                            id = existingRecipe?.id ?: 0,
                            name = name,
                            ingredients = ingredients,
                            instructions = instructions,
                            timeMin = timeMin.toIntOrNull() ?: 0,
                            difficulty = difficulty
                        )
                        viewModel.addOrUpdateRecipe(newRecipe)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Burgundy)
                ) { Text("Зберегти") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsRecipeScreen(navController: NavController, viewModel: RecipeViewModel, recipeId: Int) {
    val recipe by viewModel.getRecipeById(recipeId).collectAsState(initial = null)

    val currentRecipe = recipe
    if (currentRecipe == null) {
        Text("Рецепт не знайдено")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Деталі рецепту", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Burgundy),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            Text(text = currentRecipe.name, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Burgundy)
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Інгредієнти:", fontWeight = FontWeight.Bold)
            Text(text = currentRecipe.ingredients)
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Інструкції:", fontWeight = FontWeight.Bold)
            Text(text = currentRecipe.instructions)
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Час: ${currentRecipe.timeMin} хвилин", fontWeight = FontWeight.Bold, color = Color.DarkGray)

            Box(
                modifier = Modifier.padding(top = 8.dp).background(DustyRose, RoundedCornerShape(8.dp)).padding(8.dp)
            ) {
                Text(text = "Складність: ${currentRecipe.difficulty}", color = Burgundy, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = {
                        viewModel.deleteRecipe(currentRecipe)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Видалити") }

                Button(
                    onClick = { navController.navigate("add/${currentRecipe.id}") },
                    colors = ButtonDefaults.buttonColors(containerColor = Burgundy)
                ) { Text("Редагувати") }
            }
        }
    }
}