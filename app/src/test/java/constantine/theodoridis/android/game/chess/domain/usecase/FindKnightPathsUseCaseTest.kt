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

import constantine.theodoridis.android.game.chess.domain.builder.FindKnightPathsRequestBuilder
import constantine.theodoridis.android.game.chess.domain.builder.KnightPathBuilder
import constantine.theodoridis.android.game.chess.domain.entity.KnightPath
import constantine.theodoridis.android.game.chess.domain.entity.KnightPathsAlgorithm
import constantine.theodoridis.android.game.chess.domain.repository.KnightPathRepository
import constantine.theodoridis.android.game.chess.domain.repository.PreferenceRepository
import constantine.theodoridis.android.game.chess.domain.repository.StringRepository
import constantine.theodoridis.android.game.chess.domain.request.FindKnightPathsRequest
import constantine.theodoridis.android.game.chess.domain.response.FindKnightPathsResponse
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
        private const val PREFERRED_BOARD_SIZE = 0
        private const val PREFERRED_MOVES = 0
        private const val SOURCE_X = 0
        private const val SOURCE_Y = 0
        private const val DESTINATION_X = 0
        private const val DESTINATION_Y = 0
        private const val SOLUTION_ERROR_MESSAGE = "Solution error message"
        private const val EMPTY_STRING = ""
        private val NO_SOLUTION = listOf<KnightPath>()
        private val SOLUTION = listOf(KnightPathBuilder().build())
    }

    @Rule
    @JvmField
    val mockitoRule = MockitoJUnit.rule()!!

    @Mock
    private lateinit var mockPreferenceRepository: PreferenceRepository

    @Mock
    private lateinit var mockKnightPathsAlgorithm: KnightPathsAlgorithm

    @Mock
    private lateinit var mockStringRepository: StringRepository

    @Mock
    private lateinit var mockKnightPathRepository: KnightPathRepository

    private lateinit var useCase: UseCase<FindKnightPathsRequest, FindKnightPathsResponse>

    @Before
    fun setUp() {
        useCase = FindKnightPathsUseCase(
            mockPreferenceRepository,
            mockKnightPathsAlgorithm,
            mockStringRepository,
            mockKnightPathRepository
        )
    }

    @Test
    fun shouldSendResponseThatContainsErrorMessage_whenSolutionsDoesNotExist() {
        val request = FindKnightPathsRequestBuilder().build()
        `when`(mockPreferenceRepository.getPreferredBoardSize()).thenReturn(PREFERRED_BOARD_SIZE)
        `when`(mockPreferenceRepository.getPreferredMoves()).thenReturn(PREFERRED_MOVES)
        `when`(mockKnightPathsAlgorithm.execute(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(NO_SOLUTION)
        `when`(mockStringRepository.getSolutionErrorMessage()).thenReturn(SOLUTION_ERROR_MESSAGE)

        val response = useCase.execute(request)

        assertThat(response.solutionErrorMessage, `is`(SOLUTION_ERROR_MESSAGE))
    }

    @Test
    fun shouldSendResponseThatContainsSolutions() {
        val request = FindKnightPathsRequestBuilder().build()
        `when`(mockPreferenceRepository.getPreferredBoardSize()).thenReturn(PREFERRED_BOARD_SIZE)
        `when`(mockPreferenceRepository.getPreferredMoves()).thenReturn(PREFERRED_MOVES)
        `when`(mockKnightPathsAlgorithm.execute(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(SOLUTION)

        val response = useCase.execute(request)

        val inOrder = inOrder(mockKnightPathRepository, mockPreferenceRepository)
        inOrder.verify(mockKnightPathRepository).deleteSolutions()
        inOrder.verify(mockKnightPathRepository).save(SOLUTION)
        inOrder.verify(mockPreferenceRepository).savePreferredBoardSize(PREFERRED_BOARD_SIZE)
        inOrder.verify(mockPreferenceRepository).saveSource(SOURCE_X, SOURCE_Y)
        inOrder.verify(mockPreferenceRepository).saveDestination(DESTINATION_X, DESTINATION_Y)
        assertThat(response.solutionErrorMessage, `is`(EMPTY_STRING))
    }
}