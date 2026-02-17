package fr.isen.taraufau.thegreatestcocktailapp

import com.google.gson.annotations.SerializedName

// Réponse pour un cocktail détaillé ou random
data class CocktailResponse(
    @SerializedName("drinks") val drinks: List<CocktailDetail>?
)

data class CocktailDetail(
    @SerializedName("idDrink") val idDrink: String,
    @SerializedName("strDrink") val strDrink: String,
    @SerializedName("strCategory") val strCategory: String?,
    @SerializedName("strAlcoholic") val strAlcoholic: String?,
    @SerializedName("strGlass") val strGlass: String?,
    @SerializedName("strInstructions") val strInstructions: String?,
    @SerializedName("strDrinkThumb") val strDrinkThumb: String?,
    @SerializedName("strIngredient1") val strIngredient1: String?,
    @SerializedName("strIngredient2") val strIngredient2: String?,
    @SerializedName("strIngredient3") val strIngredient3: String?,
    @SerializedName("strIngredient4") val strIngredient4: String?,
    @SerializedName("strIngredient5") val strIngredient5: String?,
    @SerializedName("strMeasure1") val strMeasure1: String?,
    @SerializedName("strMeasure2") val strMeasure2: String?,
    @SerializedName("strMeasure3") val strMeasure3: String?,
    @SerializedName("strMeasure4") val strMeasure4: String?,
    @SerializedName("strMeasure5") val strMeasure5: String?
) {
    // Fonction utilitaire pour récupérer les ingrédients sous forme de liste
    fun getIngredients(): List<Pair<String, String>> {
        val ingredients = listOf(
            strIngredient1 to strMeasure1,
            strIngredient2 to strMeasure2,
            strIngredient3 to strMeasure3,
            strIngredient4 to strMeasure4,
            strIngredient5 to strMeasure5
        )
        return ingredients
            .filter { it.first != null && it.first!!.isNotBlank() }
            .map { (ingredient, measure) -> ingredient!! to (measure ?: "") }
    }
}

// Réponse pour la liste des catégories
data class CategoryResponse(
    @SerializedName("drinks") val drinks: List<Category>?
)

data class Category(
    @SerializedName("strCategory") val strCategory: String
)

// Réponse pour la liste des drinks d'une catégorie
data class DrinksByCategoryResponse(
    @SerializedName("drinks") val drinks: List<DrinkSummary>?
)

data class DrinkSummary(
    @SerializedName("strDrink") val strDrink: String,
    @SerializedName("strDrinkThumb") val strDrinkThumb: String?,
    @SerializedName("idDrink") val idDrink: String
)