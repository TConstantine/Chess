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
import constantine.theodoridis.android.game.chess.domain.repository.StringRepository
import constantine.theodoridis.android.game.chess.domain.request.MainMenuRequest
import constantine.theodoridis.android.game.chess.domain.response.MainMenuResponse

class ValidateMenuInputUseCase(
    private val stringRepository: StringRepository
) : UseCase<MainMenuRequest, MainMenuResponse> {
    companion object {
        private const val MINIMUM_BOARD_SIZE = 6
        private const val MAXIMUM_BOARD_SIZE = 16
        private const val MINIMUM_MOVES = 0
        private const val NUMERIC_PATTERN = "-?\\d+(\\.\\d+)?"
    }

    override fun execute(request: MainMenuRequest): MainMenuResponse {
        var boardSizeErrorMessage = ""
        var movesErrorMessage = ""
        if (!isBoardSizeInputValid(request.boardSize)) {
            boardSizeErrorMessage = stringRepository.getString(R.string.board_size_error_message)
        }
        if (!isMovesInputValid(request.moves)) {
            movesErrorMessage = stringRepository.getString(R.string.moves_error_message)
        }
        return createResponse(boardSizeErrorMessage, movesErrorMessage)
    }

    private fun isBoardSizeInputValid(boardSize: String): Boolean {
        return boardSize != "" &&
                boardSize.matches(NUMERIC_PATTERN.toRegex()) &&
                Integer.parseInt(boardSize) >= MINIMUM_BOARD_SIZE &&
                Integer.parseInt(boardSize) <= MAXIMUM_BOARD_SIZE
    }

    private fun isMovesInputValid(moves: String): Boolean {
        return moves != "" &&
                moves.matches(NUMERIC_PATTERN.toRegex()) &&
                Integer.parseInt(moves) > MINIMUM_MOVES
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