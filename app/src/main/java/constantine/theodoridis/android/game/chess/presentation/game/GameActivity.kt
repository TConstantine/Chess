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

package constantine.theodoridis.android.game.chess.presentation.game

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.room.Room
import constantine.theodoridis.android.game.chess.R
import constantine.theodoridis.android.game.chess.data.database.ApplicationDatabase
import constantine.theodoridis.android.game.chess.data.datasource.ResourcesDataSource
import constantine.theodoridis.android.game.chess.data.datasource.SQLiteDatabaseDataSource
import constantine.theodoridis.android.game.chess.data.datasource.SharedPreferencesDataSource
import constantine.theodoridis.android.game.chess.data.repository.KnightPathDepository
import constantine.theodoridis.android.game.chess.data.repository.PreferenceDepository
import constantine.theodoridis.android.game.chess.data.repository.StringDepository
import constantine.theodoridis.android.game.chess.domain.entity.BFSAlgorithm
import constantine.theodoridis.android.game.chess.domain.usecase.FindKnightPathsUseCase
import constantine.theodoridis.android.game.chess.domain.usecase.LoadGameUseCase
import constantine.theodoridis.android.game.chess.presentation.game.model.FindKnightPathsViewModel
import constantine.theodoridis.android.game.chess.presentation.game.model.LoadGameViewModel
import constantine.theodoridis.android.game.chess.presentation.settings.SettingsActivity
import io.reactivex.android.schedulers.AndroidSchedulers

class GameActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener, ChessBoardView.OnTouchEventListener {
    companion object {
        private const val EMPTY_STRING = ""
    }

    private lateinit var chessBoardView: ChessBoardView
    private lateinit var resetButton: Button
    private lateinit var solutionView: TextView
    private var clickCounter = 0
    private var sourceX = -1
    private var sourceY = -1
    private lateinit var presenter: GamePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        chessBoardView = findViewById(R.id.chess_board_view)
        resetButton = findViewById(R.id.reset)
        solutionView = findViewById(R.id.solutions)
        chessBoardView.setOnTouchEventListener(this)
        resetButton.setOnClickListener {
            clickCounter = 0
            sourceX = -1
            sourceY = -1
            chessBoardView.reset()
            chessBoardView.invalidate()
            solutionView.text = ""
        }
        val database = Room.databaseBuilder(
            applicationContext,
            ApplicationDatabase::class.java,
            "chess"
        ).build()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        val resourceDataSource = ResourcesDataSource(resources)
        val preferenceDataSource = SharedPreferencesDataSource(sharedPreferences)
        val databaseDataSource = SQLiteDatabaseDataSource(database)
        val preferenceRepository = PreferenceDepository(
            resourceDataSource,
            preferenceDataSource
        )
        val knightPathRepository = KnightPathDepository(databaseDataSource)
        val stringRepository = StringDepository(resourceDataSource)
        val knightPathsAlgorithm = BFSAlgorithm()
        val loadGameUseCase = LoadGameUseCase(
            preferenceRepository,
            knightPathRepository
        )
        val findKnightPathsUseCase = FindKnightPathsUseCase(
            preferenceRepository,
            knightPathsAlgorithm,
            stringRepository,
            knightPathRepository
        )
        presenter = ViewModelProvider(this, object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass == GamePresenter::class.java) {
                    return GamePresenter(
                        loadGameUseCase,
                        findKnightPathsUseCase,
                        AndroidSchedulers.mainThread()
                    ) as T
                }
                throw IllegalArgumentException("Factory cannot create ViewModel of type ${modelClass.simpleName}")
            }
        }).get(GamePresenter::class.java)
        presenter.loadGameViewModelObservable().observe(this, Observer { viewModel ->
            onLoadGame(viewModel)
        })
        presenter.findKnightPathsViewModelObservable().observe(this, Observer { viewModel ->
            onFindKnightPaths(viewModel)
        })
        presenter.onLoad()
    }

    override fun onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.game_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        // TODO
        if (key == getString(R.string.board_size_preference_key)) {
            val boardSize = sharedPreferences!!.getInt(
                getString(R.string.board_size_preference_key),
                resources.getInteger(R.integer.default_board_size)
            )
            chessBoardView.setSize(boardSize)
            chessBoardView.invalidate()
        }
    }

    override fun onTileClick(row: Int, column: Int) {
        if (clickCounter < 2) {
            if (clickCounter == 1 && (sourceX != column || sourceY != row)) {
                chessBoardView.setDestination(row, column)
                chessBoardView.invalidate()
                clickCounter++
                presenter.onBoardClick(sourceX, sourceY, column, row)
            }
            if (clickCounter == 0) {
                sourceX = column
                sourceY = row
                chessBoardView.setSource(row, column)
                chessBoardView.invalidate()
                clickCounter++
            }
        }
    }

    private fun onLoadGame(viewModel: LoadGameViewModel) {
        if (viewModel.solutions != EMPTY_STRING) {
            clickCounter = 2
        }
        chessBoardView.setSize(viewModel.boardSize)
        chessBoardView.setSource(viewModel.sourceY, viewModel.sourceX)
        chessBoardView.setDestination(viewModel.destinationY, viewModel.destinationX)
        chessBoardView.invalidate()
        solutionView.text = viewModel.solutions
    }

    private fun onFindKnightPaths(viewModel: FindKnightPathsViewModel) {
        solutionView.text = viewModel.solutions
    }
}