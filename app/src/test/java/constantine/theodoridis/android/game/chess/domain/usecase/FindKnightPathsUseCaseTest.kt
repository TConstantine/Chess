/*
 *  Copyright (C) 2020 Constantine Theodoridis
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package constantine.theodoridis.android.game.chess.domain.usecase

import constantine.theodoridis.android.game.chess.domain.builder.GameRequestBuilder
import constantine.theodoridis.android.game.chess.domain.entity.KnightPath
import constantine.theodoridis.android.game.chess.domain.entity.KnightPathsAlgorithm
import constantine.theodoridis.android.game.chess.domain.repository.KnightPathRepository
import constantine.theodoridis.android.game.chess.domain.repository.StringRepository
import constantine.theodoridis.android.game.chess.domain.request.GameRequest
import constantine.theodoridis.android.game.chess.domain.response.GameResponse
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.inOrder
import org.mockito.junit.MockitoJUnit

class FindKnightPathsUseCaseTest {
    companion object {
        private const val SOLUTION_ERROR_MESSAGE = "Solution error message"
        private val NO_SOLUTION = listOf<KnightPath>()
        private val SOLUTION = listOf(KnightPath(arrayOf()))
    }

    @Rule
    @JvmField
    val mockitoRule = MockitoJUnit.rule()!!

    @Mock
    private lateinit var mockKnightPathsAlgorithm: KnightPathsAlgorithm

    @Mock
    private lateinit var mockStringRepository: StringRepository

    @Mock
    private lateinit var mockKnightPathRepository: KnightPathRepository

    private lateinit var useCase: UseCase<GameRequest, GameResponse>

    @Before
    fun setUp() {
        useCase = FindKnightPathsUseCase(
            mockKnightPathsAlgorithm,
            mockStringRepository,
            mockKnightPathRepository
        )
    }

    @Test
    fun shouldSendResponseThatContainsErrorMessage_whenSolutionsDoesNotExist() {
        val request = GameRequestBuilder().build()
        `when`(mockKnightPathsAlgorithm.execute(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(NO_SOLUTION)
        `when`(mockStringRepository.getString(anyInt())).thenReturn(SOLUTION_ERROR_MESSAGE)

        val response = useCase.execute(request)

        assertThat(response.solutionErrorMessage, `is`(SOLUTION_ERROR_MESSAGE))
    }

    @Test
    fun shouldSendResponseThatContainsSolutions() {
        val request = GameRequestBuilder().build()
        `when`(mockKnightPathsAlgorithm.execute(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(SOLUTION)

        val response = useCase.execute(request)

        val inOrder = inOrder(mockKnightPathRepository)
        inOrder.verify(mockKnightPathRepository).deleteSolutions()
        inOrder.verify(mockKnightPathRepository).save(SOLUTION)
        assertThat(response.solutionErrorMessage, `is`(""))
    }
}