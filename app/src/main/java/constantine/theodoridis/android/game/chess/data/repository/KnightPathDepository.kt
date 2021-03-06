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

class KnightPathDepository(private val databaseDataSource: DatabaseDataSource): KnightPathRepository {
    override fun deleteSolutions() {
        databaseDataSource.deleteSolutions()
    }

    override fun loadSolutions(): List<KnightPath> {
        val solutions = mutableListOf<KnightPath>()
        databaseDataSource.loadSolutions().forEach {
            solutions.add(KnightPath(it.path.split(",").toMutableList()))
        }
        return solutions
    }

    override fun save(knightPaths: List<KnightPath>) {
        val databaseModel = mutableListOf<KnightPathDatabaseModel>()
        knightPaths.forEach {
            databaseModel.add(KnightPathDatabaseModel(it.path.joinToString(",")))
        }
        databaseDataSource.save(databaseModel)
    }
}