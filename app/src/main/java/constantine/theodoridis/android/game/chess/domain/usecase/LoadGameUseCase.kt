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

import constantine.theodoridis.android.game.chess.domain.entity.KnightPath
import constantine.theodoridis.android.game.chess.domain.repository.KnightPathRepository
import constantine.theodoridis.android.game.chess.domain.repository.PreferenceRepository
import constantine.theodoridis.android.game.chess.domain.request.LoadGameRequest
import constantine.theodoridis.android.game.chess.domain.response.LoadGameResponse

class LoadGameUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val knightPathRepository: KnightPathRepository
): UseCase<LoadGameRequest, LoadGameResponse> {
    override fun execute(request: LoadGameRequest): LoadGameResponse {
        val boardSize: Int
        val solutions: List<KnightPath>
        if (preferenceRepository.hasLastPreferredBoardSize()) {
            boardSize = preferenceRepository.getLastPreferredBoardSize()
            solutions = knightPathRepository.loadSolutions()
        }
        else {
            boardSize = preferenceRepository.getPreferredBoardSize()
            solutions = listOf()
        }
        return LoadGameResponse(
            boardSize = boardSize,
            solutions = solutions
        )
    }
}