package com.openclassrooms.joiefull.domain

import com.openclassrooms.joiefull.domain.model.Category
import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository
import com.openclassrooms.joiefull.domain.usecase.LoadProductsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LoadProductsUseCaseTest {

    private val repository = mockk<ProductRepository>()
    private val useCase = LoadProductsUseCase(repository)

    @Test
    fun execute_shouldCallRepoAndReturnFlowOfProducts() = runTest {
        val products = listOf(
            ProductDto(
                id = 1,
                name = "name1",
                category = Category.TOPS,
                likes = 100,
                pictureUrl = "url1",
                pictureDescription = "description1",
                priceInCents = 111,
                originalPriceInCents = 1110,
            ),
            ProductDto(
                id = 2,
                name = "name2",
                category = Category.TOPS,
                likes = 200,
                pictureUrl = "url2",
                pictureDescription = "description2",
                priceInCents = 222,
                originalPriceInCents = 2220,
            ),
        )

        coEvery { repository.fetchProducts() } returns flowOf(products)

        val result = useCase.execute()

        assertEquals(products, result.first())
        coVerify { repository.fetchProducts() }
    }
}