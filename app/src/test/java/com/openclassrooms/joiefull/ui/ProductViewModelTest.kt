package com.openclassrooms.joiefull.ui

import androidx.lifecycle.SavedStateHandle
import com.openclassrooms.joiefull.TestDispatcherProvider
import com.openclassrooms.joiefull.domain.model.Category
import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.usecase.LoadProductByIdUseCase
import com.openclassrooms.joiefull.domain.usecase.UpdateIsLikedUseCase
import com.openclassrooms.joiefull.domain.usecase.UpdateRatingUseCase
import com.openclassrooms.joiefull.domain.usecase.UpdateReviewUseCase
import com.openclassrooms.joiefull.ui.viewmodel.ProductViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val dispatcherProvider = TestDispatcherProvider(testDispatcher)
    private lateinit var loadProductByIdUseCase: LoadProductByIdUseCase
    private lateinit var updateIsLikedUseCase: UpdateIsLikedUseCase
    private lateinit var updateRatingUseCase: UpdateRatingUseCase
    private lateinit var updateReviewUseCase: UpdateReviewUseCase
    private lateinit var savedState: SavedStateHandle
    private lateinit var viewModel: ProductViewModel
    private lateinit var product: ProductDto
    private lateinit var updatedProduct: ProductDto

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        loadProductByIdUseCase = mockk<LoadProductByIdUseCase>()
        updateIsLikedUseCase = mockk<UpdateIsLikedUseCase>()
        updateRatingUseCase = mockk<UpdateRatingUseCase>()
        updateReviewUseCase = mockk<UpdateReviewUseCase>()
        savedState = SavedStateHandle(mapOf("reviewState_product1" to "review"))

        viewModel = ProductViewModel(
            savedState = savedState,
            dispatcherProvider = dispatcherProvider,
            loadProductByIdUseCase = loadProductByIdUseCase,
            updateIsLikedUseCase = updateIsLikedUseCase,
            updateRatingUseCase = updateRatingUseCase,
            updateReviewUseCase = updateReviewUseCase
        )

        product = ProductDto(
            id = 1,
            name = "name1",
            category = Category.TOPS,
            likes = 100,
            isLiked = false,
            rating = 1,
            pictureUrl = "url1",
            pictureDescription = "description1",
            priceInCents = 111,
            originalPriceInCents = 1110,
        )

        updatedProduct = ProductDto(
            id = 1,
            name = "name1",
            category = Category.TOPS,
            likes = 100,
            isLiked = true,
            review = "newReview",
            rating = 5,
            pictureUrl = "url1",
            pictureDescription = "description1",
            priceInCents = 111,
            originalPriceInCents = 1110,
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadProductById_shouldUpdateUiState() = runTest {

        val idCapture = slot<Long>()
        coEvery { loadProductByIdUseCase.execute(capture(idCapture)) } returns product

        viewModel.loadProductById(1)
        advanceUntilIdle()

        assertEquals(1, idCapture.captured)

        val state = viewModel.productUiState.value
        assertTrue(state is ProductViewModel.ProductUiState.ProductFound)

        val foundState = state as ProductViewModel.ProductUiState.ProductFound
        assertEquals(product.id, foundState.product.id)
        assertEquals("review", foundState.product.review)

        coVerify { loadProductByIdUseCase.execute(any()) }
    }

    @Test
    fun loadProductById_withException_shouldUpdateUiState() = runTest {

        val idCapture = slot<Long>()
        coEvery {
            loadProductByIdUseCase.execute(capture(idCapture))
        } throws NoSuchElementException("")

        viewModel.loadProductById(1)
        advanceUntilIdle()

        assertEquals(1, idCapture.captured)

        val state = viewModel.productUiState.value
        assertTrue(state is ProductViewModel.ProductUiState.NoProduct)

        coVerify { loadProductByIdUseCase.execute(any()) }
    }

    @Test
    fun toggleLikeState_shouldUpdateProduct() = runTest {

        val idCapture = slot<Long>()
        val likeCapture = slot<Boolean>()
        coEvery { loadProductByIdUseCase.execute(any()) } returns product
        coEvery {
            updateIsLikedUseCase.execute(
                capture(idCapture),
                capture(likeCapture)
            )
        } returns updatedProduct
        viewModel.loadProductById(1)
        advanceUntilIdle()

        viewModel.toggleLikeState()
        advanceUntilIdle()

        assertEquals(1, idCapture.captured)
        assertNotEquals(product.isLiked, likeCapture.captured)

        val state = viewModel.productUiState.value
        assertTrue(state is ProductViewModel.ProductUiState.ProductFound)

        val foundState = state as ProductViewModel.ProductUiState.ProductFound
        assertEquals(updatedProduct.isLiked, foundState.product.isLiked)

        coVerify {
            loadProductByIdUseCase.execute(any())
            updateIsLikedUseCase.execute(any(), any())
        }
    }

    @Test
    fun toggleLikeState_withException_shouldUpdateProduct() = runTest {

        val idCapture = slot<Long>()
        val likeCapture = slot<Boolean>()
        coEvery { loadProductByIdUseCase.execute(any()) } returns product
        coEvery {
            updateIsLikedUseCase.execute(
                capture(idCapture),
                capture(likeCapture)
            )
        } throws NoSuchElementException("")
        viewModel.loadProductById(1)
        advanceUntilIdle()

        viewModel.toggleLikeState()
        advanceUntilIdle()

        assertEquals(1, idCapture.captured)
        assertNotEquals(product.isLiked, likeCapture.captured)

        val state = viewModel.productUiState.value
        assertTrue(state is ProductViewModel.ProductUiState.NoProduct)

        coVerify {
            loadProductByIdUseCase.execute(any())
            updateIsLikedUseCase.execute(any(), any())
        }
    }

    @Test
    fun onReviewChanged_shouldUpdateUiState() = runTest {

        coEvery { loadProductByIdUseCase.execute(any()) } returns product
        viewModel.loadProductById(1)
        advanceUntilIdle()

        viewModel.onReviewChanged("newReview")

        val state = viewModel.productUiState.value
        assertTrue(state is ProductViewModel.ProductUiState.ProductFound)
        val foundState = state as ProductViewModel.ProductUiState.ProductFound


        assertEquals("newReview", foundState.product.review)
        assertEquals("newReview", savedState["reviewState_product1"])
        coVerify { loadProductByIdUseCase.execute(any()) }
    }

    @Test
    fun onReviewSubmitted_shouldUpdateProduct() = runTest {

        val idCapture = slot<Long>()
        val reviewCapture = slot<String>()
        coEvery { loadProductByIdUseCase.execute(any()) } returns product
        coEvery {
            updateReviewUseCase.execute(
                capture(idCapture),
                capture(reviewCapture)
            )
        } returns updatedProduct
        viewModel.loadProductById(1)
        advanceUntilIdle()

        viewModel.onReviewSubmitted("newReview")
        advanceUntilIdle()

        assertEquals(1, idCapture.captured)
        assertEquals("newReview", reviewCapture.captured)

        val state = viewModel.productUiState.value
        assertTrue(state is ProductViewModel.ProductUiState.ProductFound)

        val foundState = state as ProductViewModel.ProductUiState.ProductFound
        assertEquals(updatedProduct.review, foundState.product.review)

        coVerify {
            loadProductByIdUseCase.execute(any())
            updateReviewUseCase.execute(any(), any())
        }
    }

    @Test
    fun onReviewSubmitted_withException_shouldUpdateProduct() = runTest {

        val idCapture = slot<Long>()
        val reviewCapture = slot<String>()
        coEvery { loadProductByIdUseCase.execute(any()) } returns product
        coEvery {
            updateReviewUseCase.execute(
                capture(idCapture),
                capture(reviewCapture)
            )
        } throws NoSuchElementException("")
        viewModel.loadProductById(1)
        advanceUntilIdle()

        viewModel.onReviewSubmitted("newReview")
        advanceUntilIdle()

        assertEquals(1, idCapture.captured)
        assertEquals("newReview", reviewCapture.captured)

        val state = viewModel.productUiState.value
        assertTrue(state is ProductViewModel.ProductUiState.NoProduct)

        coVerify {
            loadProductByIdUseCase.execute(any())
            updateReviewUseCase.execute(any(), any())
        }
    }

    @Test
    fun onRatingChanged_shouldUpdateProduct() = runTest {

        val idCapture = slot<Long>()
        val ratingCapture = slot<Int>()
        coEvery { loadProductByIdUseCase.execute(any()) } returns product
        coEvery {
            updateRatingUseCase.execute(
                capture(idCapture),
                capture(ratingCapture)
            )
        } returns updatedProduct
        viewModel.loadProductById(1)
        advanceUntilIdle()

        viewModel.onRatingChanged(5)
        advanceUntilIdle()

        assertEquals(1, idCapture.captured)
        assertEquals(5, ratingCapture.captured)

        val state = viewModel.productUiState.value
        assertTrue(state is ProductViewModel.ProductUiState.ProductFound)

        val foundState = state as ProductViewModel.ProductUiState.ProductFound
        assertEquals(updatedProduct.rating, foundState.product.rating)

        coVerify {
            loadProductByIdUseCase.execute(any())
            updateRatingUseCase.execute(any(), any())
        }
    }

    @Test
    fun onRatingChanged_withException_shouldUpdateProduct() = runTest {

        val idCapture = slot<Long>()
        val ratingCapture = slot<Int>()
        coEvery { loadProductByIdUseCase.execute(any()) } returns product
        coEvery {
            updateRatingUseCase.execute(
                capture(idCapture),
                capture(ratingCapture)
            )
        } throws NoSuchElementException("")
        viewModel.loadProductById(1)
        advanceUntilIdle()

        viewModel.onRatingChanged(5)
        advanceUntilIdle()

        assertEquals(1, idCapture.captured)
        assertEquals(5, ratingCapture.captured)

        val state = viewModel.productUiState.value
        assertTrue(state is ProductViewModel.ProductUiState.NoProduct)

        coVerify {
            loadProductByIdUseCase.execute(any())
            updateRatingUseCase.execute(any(), any())
        }
    }

}