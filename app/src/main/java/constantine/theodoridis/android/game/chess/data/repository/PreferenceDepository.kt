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
            resourceDataSource.getString(R.string.last_preferred_board_size_key),
            resourceDataSource.getInteger(R.integer.default_board_size)
        )
    }

    override fun getPreferredBoardSize(): Int {
        return preferenceDataSource.getInt(
            resourceDataSource.getString(R.string.preferred_board_size_key),
            resourceDataSource.getInteger(R.integer.default_board_size)
        )
    }

    override fun getPreferredMoves(): Int {
        return preferenceDataSource.getInt(
            resourceDataSource.getString(R.string.preferred_moves_key),
            resourceDataSource.getInteger(R.integer.default_moves)
        )
    }

    override fun hasLastPreferredBoardSize(): Boolean {
        return preferenceDataSource.contains(
            resourceDataSource.getString(R.string.last_preferred_board_size_key)
        )
    }

    override fun saveLastPreferredBoardSize() {
        preferenceDataSource.putInt(
            resourceDataSource.getString(R.string.last_preferred_board_size_key),
            getPreferredBoardSize()
        )
    }
}