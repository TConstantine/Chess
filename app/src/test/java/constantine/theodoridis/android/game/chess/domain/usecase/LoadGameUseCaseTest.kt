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

import constantine.theodoridis.android.game.chess.domain.builder.KnightPathBuilder
import constantine.theodoridis.android.game.chess.domain.builder.LoadGameRequestBuilder
import constantine.theodoridis.android.game.chess.domain.repository.KnightPathRepository
import constantine.theodoridis.android.game.chess.domain.repository.PreferenceRepository
import constantine.theodoridis.android.game.chess.domain.request.LoadGameRequest
import constantine.theodoridis.android.game.chess.domain.response.LoadGameResponse
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit

class LoadGameUseCaseTest {
    companion object {
        private const val BOARD_SIZE = 0
        private val SOLUTIONS = listOf(KnightPathBuilder().build())
        private val REQUEST = LoadGameRequestBuilder().build()
    }

    @Rule
    @JvmField
    val mockitoRule = MockitoJUnit.rule()!!

    @Mock
    private lateinit var mockPreferenceRepository: PreferenceRepository

    @Mock
    private lateinit var mockKnightPathRepository: KnightPathRepository

    private lateinit var useCase: UseCase<LoadGameRequest, LoadGameResponse>

    @Before
    fun setUp() {
        useCase = LoadGameUseCase(mockPreferenceRepository, mockKnightPathRepository)
    }

    @Test
    fun shouldReturnResponseThatContainsLastSavedSolutions() {
        `when`(mockPreferenceRepository.hasLastPreferredBoardSize()).thenReturn(true)
        `when`(mockPreferenceRepository.getLastPreferredBoardSize()).thenReturn(BOARD_SIZE)
        `when`(mockKnightPathRepository.loadSolutions()).thenReturn(SOLUTIONS)

        val response = useCase.execute(REQUEST)

        assertThat(response.boardSize, `is`(BOARD_SIZE))
        assertThat(response.solutions.isEmpty(), `is`(false))
    }

    @Test
    fun shouldReturnResponseWithoutSolutions() {
        `when`(mockPreferenceRepository.hasLastPreferredBoardSize()).thenReturn(false)
        `when`(mockPreferenceRepository.getPreferredBoardSize()).thenReturn(BOARD_SIZE)

        val response = useCase.execute(REQUEST)

        assertThat(response.boardSize, `is`(BOARD_SIZE))
        assertThat(response.solutions.isEmpty(), `is`(true))
    }
}