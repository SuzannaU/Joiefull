package com.openclassrooms.joiefull.domain

import com.openclassrooms.joiefull.domain.model.Category
import com.openclassrooms.joiefull.domain.model.ProductDto
import com.openclassrooms.joiefull.domain.repository.ProductRepository
import com.openclassrooms.joiefull.domain.usecase.LoadProductByIdUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LoadProductsByIdUseCaseTest {

    private val repository = mockk<ProductRepository>()
    private val useCase = LoadProductByIdUseCase(repository)

    @Test
    fun execute_shouldCallRepoAndReturnCandidate() = runTest {
        val productDto =
            ProductDto(
                id = 1,
                name = "name1",
                category = Category.TOPS,
                likes = 100,
                pictureUrl = "url1",
                pictureDescription = "description1",
                priceInCents = 111,
                originalPriceInCents = 1110,
            )

        val idCapture = slot<Long>()
        coEvery { repository.fetchProductById(capture(idCapture)) } returns productDto

        val result = useCase.execute(1)

        assertEquals(productDto.id, idCapture.captured)
        assertEquals(productDto.id, result.id)
        coVerify { repository.fetchProductById(any()) }
    }

    @Test
    fun execute_withNoProduct_shouldCallRepoAndThrow() = runTest {

        val idCapture = slot<Long>()
        coEvery { repository.fetchProductById(capture(idCapture)) } returns null

        assertThrows<NoSuchElementException> {
            useCase.execute(1)
        }

        assertEquals(1, idCapture.captured)
        coVerify { repository.fetchProductById(any()) }
    }
}