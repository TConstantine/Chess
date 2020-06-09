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

import constantine.theodoridis.android.game.chess.domain.builder.MainMenuRequestBuilder
import constantine.theodoridis.android.game.chess.domain.request.MainMenuRequest
import constantine.theodoridis.android.game.chess.domain.response.MainMenuResponse
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class ValidateMenuInputUseCaseTest {
    companion object {
        private const val BOARD_SIZE_ERROR_MESSAGE = "Board size should be between 6 and 16"
    }

    private lateinit var useCase: UseCase<MainMenuRequest, MainMenuResponse>

    @Before
    fun setUp() {
        useCase = ValidateMenuInputUseCase()
    }

    @Test
    @Parameters(
        value = [
            "5, $BOARD_SIZE_ERROR_MESSAGE",
            "17, $BOARD_SIZE_ERROR_MESSAGE"
        ]
    )
    fun shouldReturnResponseWithErrorMessage_whenBoardSizeIsInvalid(
        boardSize: Int,
        boardSizeErrorMessage: String
    ) {
        val request = MainMenuRequestBuilder()
            .withBoardSize(boardSize)
            .build()

        val response = useCase.execute(request)

        assertThat(response.boardSizeErrorMessage, `is`(boardSizeErrorMessage))
    }
}