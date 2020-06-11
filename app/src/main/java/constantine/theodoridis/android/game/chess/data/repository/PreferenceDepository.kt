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

package constantine.theodoridis.android.game.chess.data.repository

import constantine.theodoridis.android.game.chess.R
import constantine.theodoridis.android.game.chess.data.datasource.PreferenceDataSource
import constantine.theodoridis.android.game.chess.data.datasource.ResourceDataSource
import constantine.theodoridis.android.game.chess.domain.repository.PreferenceRepository

class PreferenceDepository(
    private val resourceDataSource: ResourceDataSource,
    private val preferenceDataSource: PreferenceDataSource
) : PreferenceRepository {
    override fun getLastPreferredBoardSize(): Int {
        return preferenceDataSource.getInt(
            resourceDataSource.getString(R.string.last_saved_board_size_key),
            resourceDataSource.getInteger(R.integer.default_board_size)
        )
    }

    override fun getPreferredBoardSize(): Int {
        return preferenceDataSource.getInt(
            resourceDataSource.getString(R.string.board_size_preference_key),
            resourceDataSource.getInteger(R.integer.default_board_size)
        )
    }

    override fun getPreferredMoves(): Int {
        return preferenceDataSource.getInt(
            resourceDataSource.getString(R.string.moves_preference_key),
            resourceDataSource.getInteger(R.integer.default_moves)
        )
    }

    override fun hasLastPreferredBoardSize(): Boolean {
        return preferenceDataSource.contains(
            resourceDataSource.getString(R.string.last_saved_board_size_key)
        )
    }

    override fun saveDestination(destinationX: Int, destinationY: Int) {
        preferenceDataSource.putInt(
            resourceDataSource.getString(R.string.last_saved_destination_x_key),
            destinationX
        )
        preferenceDataSource.putInt(
            resourceDataSource.getString(R.string.last_saved_destination_y_key),
            destinationY
        )
    }

    override fun savePreferredBoardSize(boardSize: Int) {
        preferenceDataSource.putInt(
            resourceDataSource.getString(R.string.last_saved_board_size_key),
            boardSize
        )
    }

    override fun saveSource(sourceX: Int, sourceY: Int) {
        preferenceDataSource.putInt(
            resourceDataSource.getString(R.string.last_saved_source_x_key),
            sourceX
        )
        preferenceDataSource.putInt(
            resourceDataSource.getString(R.string.last_saved_source_y_key),
            sourceY
        )
    }
}