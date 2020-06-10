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

import constantine.theodoridis.android.game.chess.R
import constantine.theodoridis.android.game.chess.domain.builder.MainMenuRequestBuilder
import constantine.theodoridis.android.game.chess.domain.repository.StringRepository
import constantine.theodoridis.android.game.chess.domain.request.MainMenuRequest
import constantine.theodoridis.android.game.chess.domain.response.MainMenuResponse
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit

@RunWith(JUnitParamsRunner::class)
class ValidateMenuInputUseCaseTest {
    companion object {
        private const val BOARD_SIZE = "8"
        private const val MAX_MOVES = "3"
        private const val BOARD_SIZE_ERROR_MESSAGE = "Board size error message"
        private const val MOVES_ERROR_MESSAGE = "Moves error message"
    }

    @Rule
    @JvmField
    val mockitoRule = MockitoJUnit.rule()!!

    @Mock
    private lateinit var mockStringRepository: StringRepository

    private lateinit var useCase: UseCase<MainMenuRequest, MainMenuResponse>

    @Before
    fun setUp() {
        useCase = ValidateMenuInputUseCase(mockStringRepository)
    }

    @Test
    fun shouldReturnResponseWithoutErrorMessages() {
        val request = MainMenuRequestBuilder()
            .withBoardSize(BOARD_SIZE)
            .withMoves(MAX_MOVES)
            .build()

        val response = useCase.execute(request)

        assertThat(response.boardSizeErrorMessage, `is`(""))
        assertThat(response.movesErrorMessage, `is`(""))
    }

    @Test
    @Parameters(
        value = [
            "",
            "-",
            "5",
            "17"
        ]
    )
    fun shouldReturnResponseWithBoardSizeErrorMessage_whenBoardSizeIsInvalid(boardSize: String) {
        val request = MainMenuRequestBuilder()
            .withBoardSize(boardSize)
            .build()
        `when`(mockStringRepository.getString(anyInt())).thenReturn(BOARD_SIZE_ERROR_MESSAGE)

        val response = useCase.execute(request)

        verify(mockStringRepository).getString(R.string.board_size_error_message)
        assertThat(response.boardSizeErrorMessage, `is`(BOARD_SIZE_ERROR_MESSAGE))
    }

    @Test
    @Parameters(
        value = [
            "",
            "-",
            "0"
        ]
    )
    fun shouldReturnResponseWithMovesErrorMessage_whenMovesAreInvalid(moves: String) {
        val request = MainMenuRequestBuilder()
            .withMoves(moves)
            .build()
        `when`(mockStringRepository.getString(anyInt())).thenReturn(MOVES_ERROR_MESSAGE)

        val response = useCase.execute(request)

        verify(mockStringRepository).getString(R.string.moves_error_message)
        assertThat(response.movesErrorMessage, `is`(MOVES_ERROR_MESSAGE))
    }
}