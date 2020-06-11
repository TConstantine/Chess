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
    override fun getDestinationX(): Int {
        return getIntPreference(R.string.last_saved_destination_x_key, R.integer.default_coordinate)
    }

    override fun getDestinationY(): Int {
        return getIntPreference(R.string.last_saved_destination_y_key, R.integer.default_coordinate)
    }

    override fun getLastSavedBoardSize(): Int {
        return getIntPreference(R.string.last_saved_board_size_key, R.integer.default_board_size)
    }

    override fun getPreferredBoardSize(): Int {
        return getIntPreference(R.string.board_size_preference_key, R.integer.default_board_size)
    }

    override fun getPreferredMoves(): Int {
        return getIntPreference(R.string.moves_preference_key, R.integer.default_moves)
    }

    override fun getSourceX(): Int {
        return getIntPreference(R.string.last_saved_source_x_key, R.integer.default_coordinate)
    }

    override fun getSourceY(): Int {
        return getIntPreference(R.string.last_saved_source_y_key, R.integer.default_coordinate)
    }

    override fun hasLastSavedBoardSize(): Boolean {
        return preferenceDataSource.contains(
            resourceDataSource.getString(R.string.last_saved_board_size_key)
        )
    }

    override fun saveDestination(destinationX: Int, destinationY: Int) {
        putIntPreference(R.string.last_saved_destination_x_key, destinationX)
        putIntPreference(R.string.last_saved_destination_y_key, destinationY)
    }

    override fun savePreferredBoardSize(boardSize: Int) {
        putIntPreference(R.string.last_saved_board_size_key, boardSize)
    }

    override fun saveSource(sourceX: Int, sourceY: Int) {
        putIntPreference(R.string.last_saved_source_x_key, sourceX)
        putIntPreference(R.string.last_saved_source_y_key, sourceY)
    }

    private fun getIntPreference(keyResourceId: Int, defaultValueResourceId: Int): Int {
        return preferenceDataSource.getInt(
            resourceDataSource.getString(keyResourceId),
            resourceDataSource.getInteger(defaultValueResourceId)
        )
    }

    private fun putIntPreference(keyResourceId: Int, defaultValue: Int) {
        preferenceDataSource.putInt(
            resourceDataSource.getString(keyResourceId),
            defaultValue
        )
    }
}