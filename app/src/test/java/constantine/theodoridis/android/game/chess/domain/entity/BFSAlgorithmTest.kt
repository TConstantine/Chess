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

package constantine.theodoridis.android.game.chess.domain.entity

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class BFSAlgorithmTest {
    private lateinit var knightPathsAlgorithm: KnightPathsAlgorithm

    @Before
    fun setUp() {
        knightPathsAlgorithm = BFSAlgorithm()
    }

    @Test
    @Parameters(
        value = [
            "3, 1, 2, 0, 1, 2, 1",
            "3, 1, 2, 0, 1, 1, 0",
            "3, 2, 2, 0, 0, 0, 1",
            "4, 3, 3, 0, 3, 1, 2",
            "4, 2, 3, 0, 3, 1, 0"
        ]
    )
    fun shouldReturnPathsFoundForGivenInput(
        boardSize: Int,
        moves: Int,
        sourceX: Int,
        sourceY: Int,
        destinationX: Int,
        destinationY: Int,
        numberOfPaths: Int
    ) {
        val paths = knightPathsAlgorithm.execute(boardSize, moves, sourceX, sourceY, destinationX, destinationY)
        assertThat(paths.size, `is`(numberOfPaths))
        paths.forEach { knight -> knight.path.forEach {  } }
    }
}