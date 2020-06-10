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

package constantine.theodoridis.android.game.chess.presentation.game.builder

import constantine.theodoridis.android.game.chess.presentation.game.model.LoadGameViewModel

class LoadGameViewModelBuilder {
    private var boardSize = 0
    private var solutions = ""

    fun withBoardSize(boardSize: Int): LoadGameViewModelBuilder {
        this.boardSize = boardSize
        return this
    }

    fun withSolutions(solutions: String): LoadGameViewModelBuilder {
        this.solutions = solutions
        return this
    }

    fun build(): LoadGameViewModel {
        return LoadGameViewModel(boardSize = boardSize, solutions = solutions)
    }
}