package com.openclassrooms.joiefull.di

import com.openclassrooms.joiefull.data.dao.ClothesApiService
import com.openclassrooms.joiefull.data.repository.ProductRepositoryImpl
import com.openclassrooms.joiefull.domain.repository.ProductRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/api/")
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
        )
        .build()
}

fun provideClothesApiService(retrofit: Retrofit): ClothesApiService = retrofit.create(
    ClothesApiService::class.java
)

val appModule = module {

    single { provideRetrofit() }
    single { provideClothesApiService(get())}

    single<ProductRepository> { ProductRepositoryImpl(get()) }
}