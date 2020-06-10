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

import constantine.theodoridis.android.game.chess.domain.entity.KnightPath
import constantine.theodoridis.android.game.chess.domain.response.GameResponse

class GameResponseBuilder {
    private var solutionErrorMessage = ""
    private var solutions = listOf<KnightPath>()

    fun withSolutionErrorMessage(solutionErrorMessage: String): GameResponseBuilder {
        this.solutionErrorMessage = solutionErrorMessage
        return this
    }

    fun withSolutions(solutions: List<KnightPath>): GameResponseBuilder {
        this.solutions = solutions
        return this
    }

    fun build(): GameResponse {
        return GameResponse(solutionErrorMessage = solutionErrorMessage, solutions = solutions)
    }
}