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

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import constantine.theodoridis.android.game.chess.R
import constantine.theodoridis.android.game.chess.di.AndroidInjection
import constantine.theodoridis.android.game.chess.presentation.game.model.FindKnightPathsViewModel
import constantine.theodoridis.android.game.chess.presentation.game.model.LoadGameViewModel
import javax.inject.Inject

class GameFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener,
    ChessBoardView.OnTouchEventListener {
    companion object {
        private const val EMPTY_STRING = ""
        private const val BUNDLE_CLICK_COUNTER = "BUNDLE_CLICK_COUNTER"
        private const val BUNDLE_SOURCE_X = "BUNDLE_SOURCE_X"
        private const val BUNDLE_SOURCE_Y = "BUNDLE_SOURCE_Y"
        private const val BUNDLE_DESTINATION_X = "BUNDLE_DESTINATION_X"
        private const val BUNDLE_DESTINATION_Y = "BUNDLE_DESTINATION_Y"
        private const val BUNDLE_SOLUTION = "BUNDLE_SOLUTION"
    }

    private lateinit var chessBoardView: ChessBoardView
    private lateinit var resetButton: Button
    private lateinit var solutionView: TextView

    private var clickCounter = 0
    private var sourceX = -1
    private var sourceY = -1
    private var destinationX = -1
    private var destinationY = -1

    @Inject
    lateinit var presenter: GamePresenter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_game, container, false)
        chessBoardView = rootView.findViewById(R.id.chess_board_view)
        resetButton = rootView.findViewById(R.id.reset)
        solutionView = rootView.findViewById(R.id.solutions)
        chessBoardView.setOnTouchEventListener(this)
        resetButton.setOnClickListener {
            reset()
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        presenter.loadGameViewModelObservable().observe(viewLifecycleOwner, Observer { viewModel ->
            onLoadGame(viewModel)
        })
        presenter.findKnightPathsViewModelObservable()
            .observe(viewLifecycleOwner, Observer { viewModel ->
                onFindKnightPaths(viewModel)
            })
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        if (savedInstanceState == null) {
            presenter.onLoad()
        } else {
            clickCounter = savedInstanceState.getInt(BUNDLE_CLICK_COUNTER)
            sourceX = savedInstanceState.getInt(BUNDLE_SOURCE_X)
            sourceY = savedInstanceState.getInt(BUNDLE_SOURCE_Y)
            destinationX = savedInstanceState.getInt(BUNDLE_DESTINATION_X)
            destinationY = savedInstanceState.getInt(BUNDLE_DESTINATION_Y)
            solutionView.text = savedInstanceState.getString(BUNDLE_SOLUTION)
            val boardSize = sharedPreferences
                .getInt(getString(R.string.board_size_preference_key), R.integer.default_board_size)
            chessBoardView.setSize(boardSize)
            chessBoardView.setSource(sourceY, sourceX)
            chessBoardView.setDestination(destinationY, destinationX)
            chessBoardView.invalidate()
        }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(BUNDLE_CLICK_COUNTER, clickCounter)
        outState.putInt(BUNDLE_SOURCE_X, sourceX)
        outState.putInt(BUNDLE_SOURCE_Y, sourceY)
        outState.putInt(BUNDLE_DESTINATION_X, destinationX)
        outState.putInt(BUNDLE_DESTINATION_Y, destinationY)
        outState.putString(BUNDLE_SOLUTION, solutionView.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroyView()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == getString(R.string.board_size_preference_key)) {
            val boardSize = sharedPreferences!!.getInt(key, R.integer.default_board_size)
            chessBoardView.setSize(boardSize)
            chessBoardView.resize()
            reset()
        }
    }

    override fun onTileClick(row: Int, column: Int) {
        if (clickCounter < 2) {
            if (clickCounter == 1 && (sourceX != column || sourceY != row)) {
                destinationX = column
                destinationY = row
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
        sourceX = viewModel.sourceX
        sourceY = viewModel.sourceY
        destinationX = viewModel.destinationX
        destinationY = viewModel.destinationY
        chessBoardView.setSize(viewModel.boardSize)
        chessBoardView.setSource(viewModel.sourceY, viewModel.sourceX)
        chessBoardView.setDestination(viewModel.destinationY, viewModel.destinationX)
        chessBoardView.invalidate()
        solutionView.text = viewModel.solutions

    }

    private fun onFindKnightPaths(viewModel: FindKnightPathsViewModel) {
        solutionView.text = viewModel.solutions
    }

    private fun reset() {
        clickCounter = 0
        sourceX = -1
        sourceY = -1
        destinationX = -1
        destinationY = -1
        chessBoardView.reset()
        chessBoardView.invalidate()
        solutionView.text = ""
    }
}