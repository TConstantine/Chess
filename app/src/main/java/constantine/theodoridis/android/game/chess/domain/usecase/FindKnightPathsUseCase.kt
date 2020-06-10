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
import constantine.theodoridis.android.game.chess.domain.entity.KnightPath
import constantine.theodoridis.android.game.chess.domain.entity.KnightPathsAlgorithm
import constantine.theodoridis.android.game.chess.domain.repository.KnightPathRepository
import constantine.theodoridis.android.game.chess.domain.repository.StringRepository
import constantine.theodoridis.android.game.chess.domain.request.GameRequest
import constantine.theodoridis.android.game.chess.domain.response.GameResponse

class FindKnightPathsUseCase(
    private val knightPathsAlgorithm: KnightPathsAlgorithm,
    private val stringRepository: StringRepository,
    private val knightPathRepository: KnightPathRepository
) : UseCase<GameRequest, GameResponse> {
    override fun execute(request: GameRequest): GameResponse {
        var solutionErrorMessage = ""
        val solutions = knightPathsAlgorithm.execute(
            request.sourceX,
            request.sourceY,
            request.destinationX,
            request.destinationY
        )
        if (solutions.isEmpty()) {
            solutionErrorMessage = stringRepository.getString(R.string.solution_error_message)
        }
        else {
            knightPathRepository.deleteSolutions()
            knightPathRepository.save(solutions)
        }
        return createResponse(solutionErrorMessage, solutions)
    }

    private fun createResponse(solutionErrorMessage: String, solutions: List<KnightPath>): GameResponse {
        return GameResponse(
            solutionErrorMessage = solutionErrorMessage,
            solutions = solutions
        )
    }
}