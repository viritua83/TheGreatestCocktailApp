package fr.isen.taraufau.thegreatestcocktailapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCocktailScreen(drinkId: String? = null) {
    var cocktail by remember { mutableStateOf<CocktailDetail?>(null) }
    var isFavorite by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Appel API au chargement de l'écran
    LaunchedEffect(drinkId) {
        val call = if (drinkId != null) {
            NetworkManager.api.getCocktailDetail(drinkId)
        } else {
            NetworkManager.api.getRandomCocktail()
        }

        call.enqueue(object : Callback<CocktailResponse> {
            override fun onResponse(call: Call<CocktailResponse>, response: Response<CocktailResponse>) {
                cocktail = response.body()?.drinks?.firstOrNull()
            }

            override fun onFailure(call: Call<CocktailResponse>, t: Throwable) {
                // Erreur réseau
            }
        })
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(cocktail?.strDrink ?: "Chargement...") },
                actions = {
                    IconButton(onClick = {
                        isFavorite = !isFavorite
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                if (isFavorite) "Ajouté aux favoris !"
                                else "Retiré des favoris"
                            )
                        }
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = "Favori"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (cocktail == null) {
            // Écran de chargement
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val drink = cocktail!!

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Image depuis l'URL
                AsyncImage(
                    model = drink.strDrinkThumb,
                    contentDescription = "Photo du cocktail",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = drink.strDrink, fontSize = 24.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    drink.strCategory?.let {
                        AssistChip(onClick = {}, label = { Text(it) })
                    }
                    drink.strAlcoholic?.let {
                        AssistChip(onClick = {}, label = { Text(it) })
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                drink.strGlass?.let {
                    Text(text = "🍸 $it", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Carte ingrédients
                val ingredients = drink.getIngredients()
                if (ingredients.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "🧾 Ingrédients",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            ingredients.forEach { (name, measure) ->
                                IngredientRow(name, measure)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Carte recette
                drink.strInstructions?.let { instructions ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "📒 Recette",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = instructions)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientRow(name: String, quantity: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "• $name")
        Text(text = quantity)
    }
}