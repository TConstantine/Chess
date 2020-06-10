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

package constantine.theodoridis.android.game.chess.data.datasource

import android.content.SharedPreferences
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit

class SharedPreferencesDataSourceTest {
    companion object {
        private const val KEY = "key"
        private const val DEFAULT_VALUE = 0
    }

    @Rule
    @JvmField
    val mockitoRule = MockitoJUnit.rule()!!
    
    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    private lateinit var preferenceDataSource: PreferenceDataSource

    @Before
    fun setUp() {
        preferenceDataSource = SharedPreferencesDataSource(mockSharedPreferences)
    }

    @Test
    fun shouldCheckIfKeyExistsInSharedPreferences() {
        preferenceDataSource.contains(KEY)

        verify(mockSharedPreferences).contains(KEY)
    }

    @Test
    fun shouldReturnIntValueFromSharedPreferences() {
        preferenceDataSource.getInt(KEY, DEFAULT_VALUE)

        verify(mockSharedPreferences).getInt(KEY, DEFAULT_VALUE)
    }
}