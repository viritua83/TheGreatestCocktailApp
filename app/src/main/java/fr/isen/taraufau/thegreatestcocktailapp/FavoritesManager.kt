package fr.isen.taraufau.thegreatestcocktailapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object FavoritesManager {
    private const val PREFS_NAME = "cocktail_favorites"
    private const val KEY_FAVORITES = "favorites_list"
    private val gson = Gson()

    // Récupérer la liste des favoris
    fun getFavorites(context: Context): List<DrinkSummary> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_FAVORITES, null) ?: return emptyList()
        val type = object : TypeToken<List<DrinkSummary>>() {}.type
        return gson.fromJson(json, type)
    }

    // Vérifier si un cocktail est en favori
    fun isFavorite(context: Context, idDrink: String): Boolean {
        return getFavorites(context).any { it.idDrink == idDrink }
    }

    // Ajouter ou retirer un favori
    fun toggleFavorite(context: Context, drink: DrinkSummary): Boolean {
        val favorites = getFavorites(context).toMutableList()
        val exists = favorites.any { it.idDrink == drink.idDrink }

        if (exists) {
            favorites.removeAll { it.idDrink == drink.idDrink }
        } else {
            favorites.add(drink)
        }

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_FAVORITES, gson.toJson(favorites)).apply()

        return !exists  // retourne true si ajouté, false si retiré
    }
}