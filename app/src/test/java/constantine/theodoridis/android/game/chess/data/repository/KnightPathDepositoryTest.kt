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

import constantine.theodoridis.android.game.chess.data.database.model.KnightPathDatabaseModel
import constantine.theodoridis.android.game.chess.data.datasource.DatabaseDataSource
import constantine.theodoridis.android.game.chess.domain.entity.KnightPath
import constantine.theodoridis.android.game.chess.domain.repository.KnightPathRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit

class KnightPathDepositoryTest {
    companion object {
        private val SOLUTIONS = listOf(
            KnightPath(arrayOf("a3", "b3")),
            KnightPath(arrayOf("a4", "c3"))
        )
        private val KNIGHT_PATH_DATABASE_MODEL = listOf(
            KnightPathDatabaseModel("a3,b3"),
            KnightPathDatabaseModel("a4,c3")
        )
    }
    @Rule
    @JvmField
    val mockitoRule = MockitoJUnit.rule()!!

    @Mock
    private lateinit var mockDatabaseDataSource: DatabaseDataSource

    private lateinit var repository: KnightPathRepository

    @Before
    fun setUp() {
        repository = KnightPathDepository(mockDatabaseDataSource)
    }

    @Test
    fun shouldDeleteAllSolutionsFromDatabaseDataSource() {
        repository.deleteSolutions()

        verify(mockDatabaseDataSource).deleteSolutions()
    }

    @Test
    fun shouldSaveAllSolutionsToDatabaseDataSource() {
        repository.save(SOLUTIONS)

        verify(mockDatabaseDataSource).save(KNIGHT_PATH_DATABASE_MODEL)
    }
}