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

package constantine.theodoridis.android.game.chess.domain.builder

import constantine.theodoridis.android.game.chess.domain.request.MainMenuRequest

class MainMenuRequestBuilder {
    private var boardSize = 8
    private var maxMoves = 3

    fun withBoardSize(boardSize: Int): MainMenuRequestBuilder {
        this.boardSize = boardSize
        return this
    }

    fun withMaxMoves(maxMoves: Int): MainMenuRequestBuilder {
        this.maxMoves = maxMoves
        return this
    }

    fun build(): MainMenuRequest {
        return MainMenuRequest(
            boardSize = boardSize,
            maxMoves = maxMoves
        )
    }
}