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

import constantine.theodoridis.android.game.chess.domain.request.MainMenuRequest
import constantine.theodoridis.android.game.chess.domain.response.MainMenuResponse

class ValidateMenuInputUseCase : UseCase<MainMenuRequest, MainMenuResponse> {
    companion object {
        private const val MINIMUM_BOARD_SIZE = 6
        private const val MAXIMUM_BOARD_SIZE = 16
        private const val MINIMUM_MOVES = 1
        private const val BOARD_SIZE_ERROR_MESSAGE = "Board size should be between 6 and 16"
        private const val MOVES_ERROR_MESSAGE = "Max moves should be greater than 0"
    }

    override fun execute(request: MainMenuRequest): MainMenuResponse {
        var boardSizeErrorMessage = ""
        var movesErrorMessage = ""
        if (request.boardSize == "" || Integer.parseInt(request.boardSize) < MINIMUM_BOARD_SIZE || Integer.parseInt(request.boardSize) > MAXIMUM_BOARD_SIZE) {
            boardSizeErrorMessage = BOARD_SIZE_ERROR_MESSAGE
        }
        if (request.moves == "" || Integer.parseInt(request.moves) <= MINIMUM_MOVES) {
            movesErrorMessage = MOVES_ERROR_MESSAGE
        }
        return createResponse(boardSizeErrorMessage, movesErrorMessage)
    }

    private fun createResponse(
        boardSizeErrorMessage: String,
        maxMovesErrorMessage: String
    ): MainMenuResponse {
        return MainMenuResponse(
            boardSizeErrorMessage = boardSizeErrorMessage,
            movesErrorMessage = maxMovesErrorMessage
        )
    }
}