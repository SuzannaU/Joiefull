package com.openclassrooms.joiefull.domain

import com.openclassrooms.joiefull.domain.model.Category
import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository
import com.openclassrooms.joiefull.domain.usecase.UpdateRatingUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateRatingUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = mockk<ProductRepository>()
    private val useCase = UpdateRatingUseCase(repository)

    private lateinit var products: List<ProductDto>

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        products = listOf(
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
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun execute_shouldCallRepoAndReturnUpdatedProduct() = runTest {

        val ratingCapture = slot<Int>()
        coEvery { repository.fetchProducts() } returns flowOf(products)
        coEvery { repository.updateRating(any(), capture(ratingCapture)) } returns products[1].copy(rating = 5)
        repository.fetchProducts()
        assertEquals(0, products[1].rating)

        val result = useCase.execute(1, rating = 5)

        assertEquals(5, result.rating)
        assertEquals(5, ratingCapture.captured)
        coVerify {
            repository.fetchProducts()
            repository.updateRating(any(), any())
        }
    }

    @Test
    fun execute_withWrongId_shouldCallRepoAndThrow() = runTest {

        coEvery { repository.fetchProducts() } returns flowOf(products)
        coEvery { repository.updateRating(any(), any()) } returns null
        repository.fetchProducts()

        assertThrows<NoSuchElementException> {
            useCase.execute(100, 5)
        }

        coVerify {
            repository.fetchProducts()
            repository.updateRating(any(), any())
        }
    }
}