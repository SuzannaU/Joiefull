package com.openclassrooms.joiefull.data.dao

import com.openclassrooms.joiefull.data.entity.Clothe
import retrofit2.http.GET

interface ClothesApiService {
    @GET("clothes.json")
    suspend fun getClothes(): List<Clothe>
}