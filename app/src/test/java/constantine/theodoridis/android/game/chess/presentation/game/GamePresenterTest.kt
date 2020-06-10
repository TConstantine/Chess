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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import constantine.theodoridis.android.game.chess.domain.builder.FindKnightPathsResponseBuilder
import constantine.theodoridis.android.game.chess.domain.builder.KnightPathBuilder
import constantine.theodoridis.android.game.chess.domain.builder.LoadGameResponseBuilder
import constantine.theodoridis.android.game.chess.domain.entity.KnightPath
import constantine.theodoridis.android.game.chess.domain.request.FindKnightPathsRequest
import constantine.theodoridis.android.game.chess.domain.request.LoadGameRequest
import constantine.theodoridis.android.game.chess.domain.response.FindKnightPathsResponse
import constantine.theodoridis.android.game.chess.domain.response.LoadGameResponse
import constantine.theodoridis.android.game.chess.domain.usecase.UseCase
import constantine.theodoridis.android.game.chess.presentation.game.builder.FindKnightPathsViewModelBuilder
import constantine.theodoridis.android.game.chess.presentation.game.builder.LoadGameViewModelBuilder
import constantine.theodoridis.android.game.chess.presentation.game.model.FindKnightPathsViewModel
import constantine.theodoridis.android.game.chess.presentation.game.model.LoadGameViewModel
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit

class GamePresenterTest {
    companion object {
        private const val BOARD_SIZE = 8
        private const val SOLUTION_ERROR_MESSAGE = "Solution error message"
        private const val FORMATTED_SOLUTION = "1. a3 -> b3\n2. a4 -> c3"
        private const val EMPTY_STRING = ""
        private const val SOURCE_X = 0
        private const val SOURCE_Y = 0
        private const val DESTINATION_X = 0
        private const val DESTINATION_Y = 0
        private val KNIGHT_PATH_BUILDER = KnightPathBuilder()
        private val SOLUTIONS = listOf(
            KNIGHT_PATH_BUILDER.withPath(arrayOf("a3", "b3")).build(),
            KNIGHT_PATH_BUILDER.withPath(arrayOf("a4", "c3")).build()
        )
        private val NO_SOLUTIONS = listOf<KnightPath>()
    }

    @Rule
    @JvmField
    val mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    val testExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockLoadGameObserver: Observer<LoadGameViewModel>

    @Mock
    private lateinit var mockFindKnightPathsObserver: Observer<FindKnightPathsViewModel>

    @Mock
    private lateinit var mockLoadGameUseCase: UseCase<LoadGameRequest, LoadGameResponse>

    @Mock
    private lateinit var mockFindKnightPathsUseCase: UseCase<FindKnightPathsRequest, FindKnightPathsResponse>

    private lateinit var presenter: GamePresenter

    @Before
    fun setUp() {
        presenter = GamePresenter(
            mockLoadGameUseCase,
            mockFindKnightPathsUseCase,
            Schedulers.trampoline()
        )
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun cleanUp() {
        RxJavaPlugins.reset()
    }

    @Test
    fun shouldSendViewModelThatContainsLastSavedSolutions() {
        presenter.loadGameViewModelObservable().observeForever(mockLoadGameObserver)
        val response = LoadGameResponseBuilder()
            .withBoardSize(BOARD_SIZE)
            .withSolutions(SOLUTIONS)
            .build()
        val viewModel = LoadGameViewModelBuilder()
            .withBoardSize(BOARD_SIZE)
            .withSolutions(FORMATTED_SOLUTION)
            .build()
        `when`(mockLoadGameUseCase.execute(any())).thenReturn(response)

        presenter.onLoad()

        verify(mockLoadGameObserver).onChanged(viewModel)
    }

    @Test
    fun shouldSendViewModelThatContainsNoSolutions() {
        presenter.loadGameViewModelObservable().observeForever(mockLoadGameObserver)
        val response = LoadGameResponseBuilder()
            .withBoardSize(BOARD_SIZE)
            .withSolutions(NO_SOLUTIONS)
            .build()
        val viewModel = LoadGameViewModelBuilder()
            .withBoardSize(BOARD_SIZE)
            .withSolutions(EMPTY_STRING)
            .build()
        `when`(mockLoadGameUseCase.execute(any())).thenReturn(response)

        presenter.onLoad()

        verify(mockLoadGameObserver).onChanged(viewModel)
    }

    @Test
    fun shouldSendViewModelThatContainsSolutionErrorMessage() {
        presenter.findKnightPathsViewModelObservable().observeForever(mockFindKnightPathsObserver)
        val response = FindKnightPathsResponseBuilder()
            .withSolutionErrorMessage(SOLUTION_ERROR_MESSAGE)
            .build()
        val viewModel = FindKnightPathsViewModelBuilder()
            .withSolutions(SOLUTION_ERROR_MESSAGE)
            .build()
        `when`(mockFindKnightPathsUseCase.execute(any())).thenReturn(response)

        presenter.onBoardClick(SOURCE_X, SOURCE_Y, DESTINATION_X, DESTINATION_Y)

        verify(mockFindKnightPathsObserver).onChanged(viewModel)
    }

    @Test
    fun shouldSendViewModelThatContainsSolutions() {
        presenter.findKnightPathsViewModelObservable().observeForever(mockFindKnightPathsObserver)
        val response = FindKnightPathsResponseBuilder()
            .withSolutions(SOLUTIONS)
            .build()
        val viewModel = FindKnightPathsViewModelBuilder()
            .withSolutions(FORMATTED_SOLUTION)
            .build()
        `when`(mockFindKnightPathsUseCase.execute(any())).thenReturn(response)

        presenter.onBoardClick(SOURCE_X, SOURCE_Y, DESTINATION_X, DESTINATION_Y)

        verify(mockFindKnightPathsObserver).onChanged(viewModel)
    }
}