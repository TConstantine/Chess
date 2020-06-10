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

package constantine.theodoridis.android.game.chess.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import constantine.theodoridis.android.game.chess.data.database.model.KnightPathDatabaseModel

@Dao
interface KnightPathDao {
    @Query("SELECT * from knightPath")
    fun loadAll(): List<KnightPathDatabaseModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAll(knightPaths: List<KnightPathDatabaseModel>)

    @Query("DELETE FROM knightPath")
    fun deleteAll()
}