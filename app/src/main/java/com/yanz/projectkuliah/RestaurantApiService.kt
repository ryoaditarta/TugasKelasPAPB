package com.yanz.projectkuliah
import retrofit2.Call
import retrofit2.http.GET

interface RestaurantApiService {
    @GET("restaurants.json")
    fun getRestaurants(): Call<List<Restauran>>
}

