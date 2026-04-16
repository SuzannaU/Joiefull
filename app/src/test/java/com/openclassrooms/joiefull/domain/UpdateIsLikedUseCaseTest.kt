package com.openclassrooms.joiefull.domain

import com.openclassrooms.joiefull.domain.model.Category
import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository
import com.openclassrooms.joiefull.domain.usecase.UpdateIsLikedUseCase
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
class UpdateIsLikedUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = mockk<ProductRepository>()
    private val useCase = UpdateIsLikedUseCase(repository)

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

        val isLikedCapture = slot<Boolean>()
        coEvery { repository.fetchProducts() } returns flowOf(products)
        coEvery { repository.updateIsLiked(any(), capture(isLikedCapture)) } returns products[1].copy(isLiked = true)
        repository.fetchProducts()
        assertEquals(false, products[1].isLiked)

        val result = useCase.execute(1, isLiked = true)

        assertEquals(true, result.isLiked)
        assertEquals(true, isLikedCapture.captured)
        coVerify {
            repository.fetchProducts()
            repository.updateIsLiked(any(), any())
        }
    }

    @Test
    fun execute_withWrongId_shouldCallRepoAndThrow() = runTest {

        coEvery { repository.fetchProducts() } returns flowOf(products)
        coEvery { repository.updateIsLiked(any(), any()) } returns null
        repository.fetchProducts()

        assertThrows<NoSuchElementException> {
            useCase.execute(100, true)
        }

        coVerify {
            repository.fetchProducts()
            repository.updateIsLiked(any(), any())
        }
    }
}