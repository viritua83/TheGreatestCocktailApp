package fr.isen.taraufau.thegreatestcocktailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.isen.taraufau.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme
import java.net.URLEncoder
import java.net.URLDecoder
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheGreatestCocktailAppTheme {
                val navController = rememberNavController()
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ) {
                            val navItemColors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.background, // Icône sombre sur la pastille
                                selectedTextColor = MaterialTheme.colorScheme.primary,    // Texte rose fluo quand sélectionné
                                indicatorColor = MaterialTheme.colorScheme.primary,       // La pastille rose fluo
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant, // Icône grisée quand non sélectionné
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant  // Texte grisé
                            )

                            NavigationBarItem(
                                selected = currentRoute == "random",
                                onClick = { navController.navigate("random") {
                                    popUpTo("random") { inclusive = true }
                                }},
                                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                                label = { Text("Aléatoire",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 11.sp  ) },
                                colors = navItemColors // Applique les couleurs
                            )
                            NavigationBarItem(
                                selected = currentRoute == "categories",
                                onClick = { navController.navigate("categories") {
                                    popUpTo("random")
                                }},
                                icon = { Icon(Icons.Default.List, contentDescription = null) },
                                label = { Text(
                                    "Catégories",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 11.sp
                                ) },
                                colors = navItemColors
                            )
                            NavigationBarItem(
                                selected = currentRoute == "search",
                                onClick = { navController.navigate("search") {
                                    popUpTo("random")
                                }},
                                icon = { Icon(Icons.Default.Search, contentDescription = null) },
                                label = { Text("Recherche",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 11.sp  ) },
                                colors = navItemColors
                            )
                            NavigationBarItem(
                                selected = currentRoute == "favorites",
                                onClick = { navController.navigate("favorites") {
                                    popUpTo("random")
                                }},
                                icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                                label = { Text("Favoris",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 11.sp  ) },
                                colors = navItemColors
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "random",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("random") {
                            DetailCocktailScreen(
                                onCategoryClick = { category ->
                                    val encoded = URLEncoder.encode(category, "UTF-8")
                                    navController.navigate("drinks/$encoded")
                                }
                            )
                        }
                        composable("search") {
                            SearchScreen(onDrinkClick = { drinkId ->
                                navController.navigate("detail/$drinkId")
                            })
                        }
                        composable("categories") {
                            CategoriesScreen(onCategoryClick = { category ->
                                val encoded = URLEncoder.encode(category, "UTF-8")
                                navController.navigate("drinks/$encoded")
                            })
                        }
                        composable("drinks/{category}") { backStackEntry ->
                            val category = URLDecoder.decode(
                                backStackEntry.arguments?.getString("category") ?: "", "UTF-8"
                            )
                            DrinksListScreen(
                                category = category,
                                onDrinkClick = { drinkId ->
                                    navController.navigate("detail/$drinkId")
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("detail/{drinkId}") { backStackEntry ->
                            val drinkId = backStackEntry.arguments?.getString("drinkId") ?: ""
                            DetailCocktailScreen(drinkId = drinkId,
                                onCategoryClick = { category ->
                                    val encoded = URLEncoder.encode(category, "UTF-8")
                                    navController.navigate("drinks/$encoded")
                                })
                        }
                        composable("favorites") {
                            FavoritesScreen(onDrinkClick = { drinkId ->
                                navController.navigate("detail/$drinkId")
                            })
                        }
                    }
                }
            }
        }
    }
}