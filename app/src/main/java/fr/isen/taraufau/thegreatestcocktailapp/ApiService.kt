package fr.isen.taraufau.thegreatestcocktailapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random.php")
    fun getRandomCocktail(): Call<CocktailResponse>

    @GET("list.php?c=list")
    fun getCategories(): Call<CategoryResponse>

    @GET("filter.php")
    fun getDrinksByCategory(@Query("c") category: String): Call<DrinksByCategoryResponse>

    @GET("lookup.php")
    fun getCocktailDetail(@Query("i") id: String): Call<CocktailResponse>
}