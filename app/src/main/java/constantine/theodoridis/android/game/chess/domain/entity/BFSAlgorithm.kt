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

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class BFSAlgorithm : KnightPathsAlgorithm {
    companion object {
        private const val MAXIMUM_POSSIBLE_MOVES = 8
        private val POSSIBLE_VERTICAL_MOVES = intArrayOf(2, 2, -2, -2, 1, 1, -1, -1)
        private val POSSIBLE_HORIZONTAL_MOVES = intArrayOf(-1, 1, 1, -1, 2, -2, 2, -2)
        private val HORIZONTAL_MAPPINGS = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p")
        private val VERTICAL_MAPPINGS = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
    }

    override fun execute(
        boardSize: Int,
        moves: Int,
        sourceX: Int,
        sourceY: Int,
        destinationX: Int,
        destinationY: Int
    ): List<KnightPath> {
        val paths: MutableList<KnightPath> = ArrayList()
        val source = Node(sourceX, sourceY)
        val destination = Node(destinationX, destinationY)
        val visitedCells: MutableSet<Node> = HashSet()
        val queue: Queue<Node> = ArrayDeque()
        queue.add(source)
        while (!queue.isEmpty()) {
            val node = queue.poll()
            if (node!!.distance > moves) {
                return paths
            }
            if (node.x == destination.x && node.y == destination.y && moves == node.distance) {
                val path = getPath(node, mutableListOf(), boardSize)
                path.reverse()
                paths.add(KnightPath(path))
            }
            if (!visitedCells.contains(node)) {
                visitedCells.add(node)
                for (i in 0 until MAXIMUM_POSSIBLE_MOVES) {
                    val x = node.x + POSSIBLE_VERTICAL_MOVES[i]
                    val y = node.y + POSSIBLE_HORIZONTAL_MOVES[i]
                    if (isValid(x, y, boardSize)) {
                        queue.add(Node(x, y, node.distance + 1, node))
                    }
                }
            }
        }
        return paths
    }

    private fun isValid(x: Int, y: Int, boardSize: Int): Boolean {
        return !(x < 0 || y < 0 || x >= boardSize || y >= boardSize)
    }

    private fun getPath(node: Node?, path: MutableList<String>, boardSize: Int): MutableList<String> {
        if (node != null) {
            path.add(algebraicNotation(node, boardSize))
            getPath(node.parent, path, boardSize)
        }
        return path
    }

    private fun algebraicNotation(node: Node, boardSize: Int): String {
        return "N${HORIZONTAL_MAPPINGS[node.y]}${VERTICAL_MAPPINGS[boardSize - 1 - node.x]}"
    }
}