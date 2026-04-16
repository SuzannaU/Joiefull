package com.openclassrooms.joiefull.data.dao

import com.openclassrooms.joiefull.data.entity.Clothe
import retrofit2.http.GET

interface ClothesApiService {

    @GET("clothes.json")
    suspend fun getClothes(): List<Clothe>

    /* Example of function to retrieve a specific Clothe (not valid with current demo API)
    @GET("clothes")
    suspend fun getClothesById(
        @Query(value = "id") id: Long,
    ): Clothe?

     */
}