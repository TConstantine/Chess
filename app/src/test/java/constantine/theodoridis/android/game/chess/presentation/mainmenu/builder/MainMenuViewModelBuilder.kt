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

package constantine.theodoridis.android.game.chess.presentation.mainmenu.builder

import constantine.theodoridis.android.game.chess.presentation.mainmenu.model.MainMenuViewModel

class MainMenuViewModelBuilder {
    private var hasBoardSizeError = false
    private var boardSizeErrorMessage = ""
    private var hasMovesError = false
    private var movesErrorMessage = ""

    fun withBoardSizeError(): MainMenuViewModelBuilder {
        this.hasBoardSizeError = true
        return this
    }


    fun withBoardSizeErrorMessage(boardSizeErrorMessage: String): MainMenuViewModelBuilder {
        this.boardSizeErrorMessage = boardSizeErrorMessage
        return this
    }

    fun withMovesError(): MainMenuViewModelBuilder {
        this.hasMovesError = true
        return this
    }

    fun withMovesErrorMessage(movesErrorMessage: String): MainMenuViewModelBuilder {
        this.movesErrorMessage = movesErrorMessage
        return this
    }

    fun build(): MainMenuViewModel {
        return MainMenuViewModel(
            hasBoardSizeError = hasBoardSizeError,
            boardSizeErrorMessage = boardSizeErrorMessage,
            hasMovesError = hasMovesError,
            movesErrorMessage = movesErrorMessage
        )
    }
}