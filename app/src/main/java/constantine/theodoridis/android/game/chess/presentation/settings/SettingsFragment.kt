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

package constantine.theodoridis.android.game.chess.presentation.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import constantine.theodoridis.android.game.chess.R


class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var boardSizePreference: SeekBarPreference
    private lateinit var movesPreference: SeekBarPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        boardSizePreference = findPreference(resources.getString(R.string.board_size_preference_key))!!
        boardSizePreference.min = resources.getInteger(R.integer.minimum_board_size)
        boardSizePreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, value ->
                updateMovesPreference(value)
                true
            }
        movesPreference = findPreference(resources.getString(R.string.moves_preference_key))!!
        movesPreference.min = resources.getInteger(R.integer.minimum_moves)
        movesPreference.max = boardSizePreference.value * boardSizePreference.value - 1
    }

    private fun updateMovesPreference(value: Any) {
        val boardSize = Integer.parseInt(value.toString())
        val maxMoves = boardSize * boardSize - 1
        if (movesPreference.value > maxMoves) {
            movesPreference.value = maxMoves
        }
        movesPreference.max = maxMoves
    }
}