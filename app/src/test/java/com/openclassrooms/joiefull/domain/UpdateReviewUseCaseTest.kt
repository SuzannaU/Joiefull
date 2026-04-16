package com.openclassrooms.joiefull.domain

import com.openclassrooms.joiefull.domain.model.Category
import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository
import com.openclassrooms.joiefull.domain.usecase.UpdateReviewUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
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

class UpdateReviewUseCaseTest {
    private val testDispatcher = StandardTestDispatcher()
    private val repository = mockk<ProductRepository>()
    private val useCase = UpdateReviewUseCase(repository)

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

        val reviewCapture = slot<String>()
        coEvery { repository.fetchProducts() } returns flowOf(products)
        coEvery { repository.updateReview(any(), capture(reviewCapture)) } returns products[1].copy(review = "newReview")
        repository.fetchProducts()
        assertEquals("", products[1].review)

        val result = useCase.execute(1, review = "newReview")

        assertEquals("newReview", result.review)
        assertEquals("newReview", reviewCapture.captured)
        coVerify {
            repository.fetchProducts()
            repository.updateReview(any(), any())
        }
    }

    @Test
    fun execute_withWrongId_shouldCallRepoAndThrow() = runTest {

        coEvery { repository.fetchProducts() } returns flowOf(products)
        coEvery { repository.updateReview(any(), any()) } returns null
        repository.fetchProducts()

        assertThrows<NoSuchElementException> {
            useCase.execute(100, "newReview")
        }

        coVerify {
            repository.fetchProducts()
            repository.updateReview(any(), any())
        }
    }
}