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

class Node {
    val x: Int
    val y: Int
    var distance = 0
    var parent: Node? = null

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    constructor(x: Int, y: Int, distance: Int, parent: Node?) {
        this.x = x
        this.y = y
        this.distance = distance
        this.parent = parent
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val node = other as Node
        return node.x == x && node.y == y
    }

    override fun hashCode(): Int {
        var hash = 3
        hash = 37 * hash + x
        hash = 37 * hash + y
        return hash
    }
}