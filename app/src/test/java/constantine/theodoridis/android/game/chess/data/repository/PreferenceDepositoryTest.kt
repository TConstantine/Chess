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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit

class PreferenceDepositoryTest {
    companion object {
        private const val LAST_PREFERRED_BOARD_SIZE_KEY = "Last preferred board size key"
        private const val PREFERRED_BOARD_SIZE_KEY = "Preferred board size key"
        private const val DEFAULT_BOARD_SIZE = 0
        private const val PREFERRED_BOARD_SIZE = 0
        private const val MOVES_KEY = "Moves key"
        private const val DEFAULT_MOVES = 0
        private const val LAST_SAVED_SOURCE_X_KEY = "Last saved source x key"
        private const val LAST_SAVED_SOURCE_Y_KEY = "Last saved source y key"
        private const val SOURCE_X = 0
        private const val SOURCE_Y = 0
        private const val LAST_SAVED_DESTINATION_X_KEY = "Last saved destination x key"
        private const val LAST_SAVED_DESTINATION_Y_KEY = "Last saved destination y key"
        private const val DESTINATION_X = 0
        private const val DESTINATION_Y = 0
    }

    @Rule
    @JvmField
    val mockitoRule = MockitoJUnit.rule()!!

    @Mock
    private lateinit var mockResourceDataSource: ResourceDataSource

    @Mock
    private lateinit var mockPreferenceDataSource: PreferenceDataSource

    private lateinit var repository: PreferenceRepository

    @Before
    fun setUp() {
        repository = PreferenceDepository(mockResourceDataSource, mockPreferenceDataSource)
    }

    @Test
    fun shouldReturnLastPreferredBoardSizeFromPreferenceDataSource() {
        `when`(mockResourceDataSource.getString(anyInt())).thenReturn(LAST_PREFERRED_BOARD_SIZE_KEY)
        `when`(mockResourceDataSource.getInteger(anyInt())).thenReturn(DEFAULT_BOARD_SIZE)

        repository.getLastPreferredBoardSize()

        verify(mockResourceDataSource).getString(R.string.last_saved_board_size_key)
        verify(mockResourceDataSource).getInteger(R.integer.default_board_size)
        verify(mockPreferenceDataSource).getInt(LAST_PREFERRED_BOARD_SIZE_KEY, DEFAULT_BOARD_SIZE)
    }

    @Test
    fun shouldReturnPreferredBoardSizeFromPreferenceDataSource() {
        `when`(mockResourceDataSource.getString(anyInt())).thenReturn(PREFERRED_BOARD_SIZE_KEY)
        `when`(mockResourceDataSource.getInteger(anyInt())).thenReturn(DEFAULT_BOARD_SIZE)

        repository.getPreferredBoardSize()

        verify(mockResourceDataSource).getString(R.string.board_size_preference_key)
        verify(mockResourceDataSource).getInteger(R.integer.default_board_size)
        verify(mockPreferenceDataSource).getInt(PREFERRED_BOARD_SIZE_KEY, DEFAULT_BOARD_SIZE)
    }

    @Test
    fun shouldReturnPreferredMovesFromPreferenceDataSource() {
        `when`(mockResourceDataSource.getString(anyInt())).thenReturn(MOVES_KEY)
        `when`(mockResourceDataSource.getInteger(anyInt())).thenReturn(DEFAULT_MOVES)

        repository.getPreferredMoves()

        verify(mockResourceDataSource).getString(R.string.moves_preference_key)
        verify(mockResourceDataSource).getInteger(R.integer.default_moves)
        verify(mockPreferenceDataSource).getInt(MOVES_KEY, DEFAULT_MOVES)
    }

    @Test
    fun shouldCheckIfLastPreferredBoardSizeExistsInPreferenceDataSource() {
        `when`(mockResourceDataSource.getString(anyInt())).thenReturn(LAST_PREFERRED_BOARD_SIZE_KEY)

        repository.hasLastPreferredBoardSize()

        verify(mockResourceDataSource).getString(R.string.last_saved_board_size_key)
        verify(mockPreferenceDataSource).contains(LAST_PREFERRED_BOARD_SIZE_KEY)
    }

    @Test
    fun shouldSaveDestinationInPreferenceDataSource() {
        `when`(mockResourceDataSource.getString(anyInt()))
            .thenReturn(LAST_SAVED_DESTINATION_X_KEY)
            .thenReturn(LAST_SAVED_DESTINATION_Y_KEY)

        repository.saveDestination(DESTINATION_X, DESTINATION_Y)

        verify(mockResourceDataSource).getString(R.string.last_saved_destination_x_key)
        verify(mockPreferenceDataSource).putInt(LAST_SAVED_DESTINATION_X_KEY, DESTINATION_X)
        verify(mockResourceDataSource).getString(R.string.last_saved_destination_y_key)
        verify(mockPreferenceDataSource).putInt(LAST_SAVED_DESTINATION_Y_KEY, DESTINATION_Y)
    }

    @Test
    fun shouldSavePreferredBoardSizeInPreferenceDataSource() {
        `when`(mockResourceDataSource.getString(anyInt())).thenReturn(LAST_PREFERRED_BOARD_SIZE_KEY)

        repository.savePreferredBoardSize(PREFERRED_BOARD_SIZE)

        verify(mockResourceDataSource).getString(R.string.last_saved_board_size_key)
        verify(mockPreferenceDataSource).putInt(LAST_PREFERRED_BOARD_SIZE_KEY, PREFERRED_BOARD_SIZE)
    }

    @Test
    fun shouldSaveSourceInPreferenceDataSource() {
        `when`(mockResourceDataSource.getString(anyInt()))
            .thenReturn(LAST_SAVED_SOURCE_X_KEY)
            .thenReturn(LAST_SAVED_SOURCE_Y_KEY)

        repository.saveSource(SOURCE_X, SOURCE_Y)

        verify(mockResourceDataSource).getString(R.string.last_saved_source_x_key)
        verify(mockPreferenceDataSource).putInt(LAST_SAVED_SOURCE_X_KEY, SOURCE_X)
        verify(mockResourceDataSource).getString(R.string.last_saved_source_y_key)
        verify(mockPreferenceDataSource).putInt(LAST_SAVED_SOURCE_Y_KEY, SOURCE_Y)
    }
}