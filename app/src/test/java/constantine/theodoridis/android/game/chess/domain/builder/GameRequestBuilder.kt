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

import constantine.theodoridis.android.game.chess.domain.request.GameRequest

class GameRequestBuilder {
    private var sourceX = 0
    private var sourceY = 0
    private var destinationX = 0
    private var destinationY = 0

    fun withSourceX(sourceX: Int): GameRequestBuilder {
        this.sourceX = sourceX
        return this
    }

    fun withSourceY(sourceY: Int): GameRequestBuilder {
        this.sourceY = sourceY
        return this
    }

    fun withDestinationX(destinationX: Int): GameRequestBuilder {
        this.destinationX = destinationX
        return this
    }

    fun withDestinationY(destinationY: Int): GameRequestBuilder {
        this.destinationY = destinationY
        return this
    }

    fun build(): GameRequest {
        return GameRequest(
            sourceX = sourceX,
            sourceY = sourceY,
            destinationX = destinationX,
            destinationY = destinationY
        )
    }
}