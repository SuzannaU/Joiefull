package com.openclassrooms.joiefull.data

import com.openclassrooms.joiefull.data.dao.ClothesApiService
import com.openclassrooms.joiefull.data.entity.Clothe
import com.openclassrooms.joiefull.data.entity.Picture
import com.openclassrooms.joiefull.data.repository.ProductRepositoryImpl
import com.openclassrooms.joiefull.domain.model.Category
import com.openclassrooms.joiefull.domain.model.ProductDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val apiService: ClothesApiService = mockk()
    private val repository: ProductRepositoryImpl = ProductRepositoryImpl(apiService = apiService)
    private lateinit var products: List<ProductDto>
    private lateinit var clothes: List<Clothe>

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        clothes = listOf(
            Clothe(
                id = 1,
                name = "name1",
                category = Category.TOPS,
                likes = 100,
                picture = Picture(
                    url = "url1",
                    description = "description1",
                ),
                price = 1.110,
                originalPrice = 11.100,
            ),
            Clothe(
                id = 2,
                name = "name2",
                category = Category.TOPS,
                likes = 200,
                picture = Picture(
                    url = "url2",
                    description = "description2",
                ),
                price = 2.220,
                originalPrice = 22.200,
            ),
        )
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
    fun fetchProductById_shouldCallDaoAndReturnProduct() = runTest {

        coEvery { apiService.getClothes() } returns clothes

        val result = repository.fetchProductById(1L)
        assertNotNull(result)
        assertEquals(1L, result.id)
        coVerify { apiService.getClothes() }
    }

    @Test
    fun fetchProductById_withNoProductFound_shouldCallDaoAndReturnNull() = runTest {

        coEvery { apiService.getClothes() } returns clothes

        val result = repository.fetchProductById(1000L)
        assertNull(result)
        coVerify { apiService.getClothes() }
    }

    @Test
    fun fetchProducts_shouldCallDaoAndReturnFlowOfProducts() = runTest {

        coEvery { apiService.getClothes() } returns clothes

        val result = repository.fetchProducts()

        assertEquals(products, result.first())
        coVerify { apiService.getClothes() }
    }

    @Test
    fun updateRating_shouldUpdateProductsAndReturnUpdatedProduct() = runTest {

        coEvery { apiService.getClothes() } returns clothes
        repository.fetchProducts()
        assertEquals(0, repository.fetchProductById(1)?.rating)

        val result = repository.updateRating(1, 5)

        assertEquals(5, result?.rating)
        coVerify { apiService.getClothes() }
    }

    @Test
    fun updateReview_shouldUpdateProductsAndReturnUpdatedProduct() = runTest {

        coEvery { apiService.getClothes() } returns clothes
        repository.fetchProducts()
        assertEquals(true, repository.fetchProductById(1)?.review?.isEmpty())

        val result = repository.updateReview(1, "newReview")

        assertEquals("newReview", result?.review)
        coVerify { apiService.getClothes() }
    }

    @Test
    fun updateIsLiked_shouldUpdateProductsAndReturnUpdatedProduct() = runTest {

        coEvery { apiService.getClothes() } returns clothes
        repository.fetchProducts()
        assertEquals(false, repository.fetchProductById(1)?.isLiked)

        val result = repository.updateIsLiked(1, true)

        assertEquals(true, result?.isLiked)
        coVerify { apiService.getClothes() }
    }

}