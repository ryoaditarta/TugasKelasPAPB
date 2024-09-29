package com.yanz.projectkuliah

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RestaurantsViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {
    private var restInterface: RestaurantApiService
    val state = mutableStateOf(emptyList<Restauran>())

    private lateinit var restaurantCall:Call<List<Restauran>>
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl(
                "https://restaurantapp-c84e0-default-rtdb.firebaseio.com/"
            )
            .build()
        restInterface = retrofit.create(
            RestaurantApiService::class.java
        )
        getRestaurants()
    }

    override fun onCleared() {
        super.onCleared()
        restaurantCall.cancel()
    }

    fun getRestaurants() {
        restaurantCall = restInterface.getRestaurants()
        restaurantCall.enqueue(
            object : Callback<List<Restauran>> {
                override fun onResponse(
                    call: Call<List<Restauran>>,
                    response: Response<List<Restauran>>
                ) {
                    response.body()?.let { restaurants ->
                        state.value =
                            restaurants.restoreSelections()
                    }
                }
                override fun onFailure(
                    call: Call<List<Restauran>>, t: Throwable
                ) {
                    t.printStackTrace()
                }
            }
        )
    }

    fun toggleFavorite(id: Int) {
        val restaurants = state.value.toMutableList()
        val itemIndex = restaurants.indexOfFirst { it.id == id }
        val item = restaurants[itemIndex]
        restaurants[itemIndex] = item.copy(isFavorite = !item.isFavorite)
        storeSelection(restaurants[itemIndex])
        state.value = restaurants
    }

    private fun storeSelection(item: Restauran) {
        val savedToggled = stateHandle.get<List<Int>?>(FAVORITES)
            .orEmpty().toMutableList()
        if (item.isFavorite) savedToggled.add(item.id)
        else savedToggled.remove(item.id)
        stateHandle[FAVORITES] = savedToggled
    }

    private fun List<Restauran>.restoreSelections(): List<Restauran> {
        stateHandle.get<List<Int>?>(FAVORITES)?.let { selectedIds ->
            val restaurantsMap = this.associateBy { it.id }
            selectedIds.forEach { id ->
                restaurantsMap[id]?.isFavorite = true
            }
            return restaurantsMap.values.toList()
        }
        return this
    }

    companion object {
        const val FAVORITES = "favorites"
    }

}