package fr.isen.taraufau.thegreatestcocktailapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onDrinkClick: (String) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<DrinkSummary>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var hasSearched by remember { mutableStateOf(false) }

    // États pour le filtre
    var showFilterMenu by remember { mutableStateOf(false) }
    var ingredientsList by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedIngredient by remember { mutableStateOf<String?>(null) }

    // Charger la liste des ingrédients au lancement de l'écran
    LaunchedEffect(Unit) {
        NetworkManager.api.getIngredientsList().enqueue(object : Callback<IngredientListResponse> {
            override fun onResponse(call: Call<IngredientListResponse>, response: Response<IngredientListResponse>) {
                ingredientsList = response.body()?.drinks?.map { it.strIngredient1 }?.sorted() ?: emptyList()
            }
            override fun onFailure(call: Call<IngredientListResponse>, t: Throwable) {}
        })
    }

    // Fonction pour la recherche par texte (nom ou lettre)
    fun performTextSearch() {
        if (searchQuery.isBlank()) return
        isLoading = true
        hasSearched = true
        selectedIngredient = null // On réinitialise le filtre si on fait une recherche texte

        val call = if (searchQuery.length == 1) {
            NetworkManager.api.searchByFirstLetter(searchQuery)
        } else {
            NetworkManager.api.searchCocktails(searchQuery)
        }

        call.enqueue(object : Callback<CocktailResponse> {
            override fun onResponse(call: Call<CocktailResponse>, response: Response<CocktailResponse>) {
                val allResults = response.body()?.drinks ?: emptyList()
                val filtered = allResults.filter { drink ->
                    drink.strDrink.startsWith(searchQuery, ignoreCase = true)
                }
                searchResults = filtered.map {
                    DrinkSummary(it.strDrink, it.strDrinkThumb, it.idDrink)
                }
                isLoading = false
            }
            override fun onFailure(call: Call<CocktailResponse>, t: Throwable) { isLoading = false }
        })
    }

    // Fonction pour la recherche par ingrédient
    fun performIngredientFilter(ingredient: String) {
        isLoading = true
        hasSearched = true
        searchQuery = "" // On vide la barre de recherche texte

        NetworkManager.api.filterByIngredient(ingredient).enqueue(object : Callback<DrinksByCategoryResponse> {
            override fun onResponse(call: Call<DrinksByCategoryResponse>, response: Response<DrinksByCategoryResponse>) {
                searchResults = response.body()?.drinks ?: emptyList()
                isLoading = false
            }
            override fun onFailure(call: Call<DrinksByCategoryResponse>, t: Throwable) { isLoading = false }
        })
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Recherche", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
        ) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Nom du cocktail...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Rechercher") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { performTextSearch() }),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { showFilterMenu = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Filtre",
                        tint = if (selectedIngredient != null) MaterialTheme.colorScheme.primary else LocalContentColor.current
                    )
                    Text("Filtre", fontSize = 12.sp)
                }

                DropdownMenu(
                    expanded = showFilterMenu,
                    onDismissRequest = { showFilterMenu = false },
                    modifier = Modifier.fillMaxHeight(0.6f).background(MaterialTheme.colorScheme.surface),

                ) {
                    ingredientsList.chunked(2).forEach { rowIngredients ->
                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                            rowIngredients.forEach { ingredient ->
                                val isChecked = selectedIngredient == ingredient

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            if (isChecked) {
                                                // Si déjà coché, on décoche et on vide les résultats
                                                selectedIngredient = null
                                                searchResults = emptyList()
                                                hasSearched = false
                                            } else {
                                                // Sinon on coche et on lance la recherche
                                                selectedIngredient = ingredient
                                                performIngredientFilter(ingredient)
                                            }
                                            showFilterMenu = false // On ferme le menu après le clic
                                        }
                                        .padding(vertical = 4.dp)
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = null, // Géré par le clic sur la Row entière
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = MaterialTheme.colorScheme.primary, // Le carré devient Rose Fluo
                                            checkmarkColor = MaterialTheme.colorScheme.background, // La coche ("v") devient sombre
                                            uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                    Text(
                                        text = ingredient,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (selectedIngredient != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Filtré par : $selectedIngredient",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // RÉSULTATS
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (hasSearched && searchResults.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aucun résultat trouvé.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(searchResults) { drink ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onDrinkClick(drink.idDrink) },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)),
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = drink.strDrinkThumb,
                                    contentDescription = drink.strDrink,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = drink.strDrink, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}