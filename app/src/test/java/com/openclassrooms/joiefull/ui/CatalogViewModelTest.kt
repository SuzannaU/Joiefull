package com.openclassrooms.joiefull.ui

import com.openclassrooms.joiefull.TestDispatcherProvider
import com.openclassrooms.joiefull.domain.model.Category
import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.usecase.LoadProductsUseCase
import com.openclassrooms.joiefull.ui.viewmodel.CatalogViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val dispatcherProvider = TestDispatcherProvider(testDispatcher)
    private lateinit var loadProductsUseCase: LoadProductsUseCase
    private lateinit var viewModel: CatalogViewModel
    private lateinit var products: List<ProductDto>

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        loadProductsUseCase = mockk<LoadProductsUseCase>()
        viewModel = CatalogViewModel(dispatcherProvider, loadProductsUseCase)

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
                category = Category.ACCESSORIES,
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
    fun loadAllProducts_shouldUpdateUiState() = runTest {

        coEvery { loadProductsUseCase.execute() } returns flowOf(products)
        advanceUntilIdle()

        val state = viewModel.catalogUiState.value
        assertTrue(state is CatalogViewModel.CatalogUiState.ProductsFound)

        val foundState = state as CatalogViewModel.CatalogUiState.ProductsFound
        assertEquals(
            products.filter { it.category == Category.TOPS }.size,
            foundState.groupedProducts[Category.TOPS]?.size
        )

        coVerify { loadProductsUseCase.execute() }
    }

    @Test
    fun loadAllProducts_withNoProducts_shouldUpdateUiState() = runTest {

        coEvery { loadProductsUseCase.execute() } returns flowOf(emptyList())
        advanceUntilIdle()

        val state = viewModel.catalogUiState.value
        assertTrue(state is CatalogViewModel.CatalogUiState.NoProducts)

        coVerify { loadProductsUseCase.execute() }
    }
}