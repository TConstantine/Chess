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

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import constantine.theodoridis.android.game.chess.data.database.ApplicationDatabase
import constantine.theodoridis.android.game.chess.data.database.builder.KnightPathDatabaseModelBuilder
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SQLiteDatabaseDataSourceTest {
    companion object {
        private val KNIGHT_PATH_DATABASE_MODEL_BUILDER = KnightPathDatabaseModelBuilder()
        private val KNIGHT_PATH_DATABASE_MODEL = listOf(
            KNIGHT_PATH_DATABASE_MODEL_BUILDER.build(),
            KNIGHT_PATH_DATABASE_MODEL_BUILDER.build()
        )
    }

    @Rule
    @JvmField
    val testExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ApplicationDatabase
    private lateinit var databaseDataSource: DatabaseDataSource

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ApplicationDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        databaseDataSource = SQLiteDatabaseDataSource(database)
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun shouldSaveAllKnightPathsToDatabase() {
        databaseDataSource.save(KNIGHT_PATH_DATABASE_MODEL)

        val databaseModel = databaseDataSource.loadSolutions()
        assertThat(databaseModel.isEmpty(), `is`(false))
        assertThat(databaseModel.size, `is`(KNIGHT_PATH_DATABASE_MODEL.size))
    }

    @Test
    fun shouldDeleteAllKnightPathsFromDatabase() {
        databaseDataSource.save(KNIGHT_PATH_DATABASE_MODEL)

        databaseDataSource.deleteSolutions()

        val databaseModel = databaseDataSource.loadSolutions()
        assertThat(databaseModel.isEmpty(), `is`(true))
    }
}