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
import constantine.theodoridis.android.game.chess.domain.builder.GameResponseBuilder
import constantine.theodoridis.android.game.chess.domain.entity.KnightPath
import constantine.theodoridis.android.game.chess.domain.request.GameRequest
import constantine.theodoridis.android.game.chess.domain.response.GameResponse
import constantine.theodoridis.android.game.chess.domain.usecase.UseCase
import constantine.theodoridis.android.game.chess.presentation.game.builder.GameViewModelBuilder
import constantine.theodoridis.android.game.chess.presentation.game.model.GameViewModel
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
        private const val SOLUTION_ERROR_MESSAGE = "Solution error message"
        private const val FORMATTED_SOLUTION = "1. a3 -> b3\n2. a4 -> c3"
        private const val SOURCE_X = 0
        private const val SOURCE_Y = 0
        private const val DESTINATION_X = 0
        private const val DESTINATION_Y = 0
        private val SOLUTIONS = listOf(
            KnightPath(arrayOf("a3", "b3")),
            KnightPath(arrayOf("a4", "c3"))
        )
    }

    @Rule
    @JvmField
    val mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    val testExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockObserver: Observer<GameViewModel>

    @Mock
    private lateinit var mockUseCase: UseCase<GameRequest, GameResponse>

    private lateinit var presenter: GamePresenter

    @Before
    fun setUp() {
        presenter = GamePresenter(mockUseCase, Schedulers.trampoline())
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun cleanUp() {
        RxJavaPlugins.reset()
    }

    @Test
    fun shouldNotifyObserversWithViewModelThatContainsSolutionErrorMessage() {
        presenter.viewModelObservable().observeForever(mockObserver)
        val response = GameResponseBuilder()
            .withSolutionErrorMessage(SOLUTION_ERROR_MESSAGE)
            .build()
        val viewModel = GameViewModelBuilder()
            .withSolutions(SOLUTION_ERROR_MESSAGE)
            .build()
        `when`(mockUseCase.execute(any())).thenReturn(response)

        presenter.onClick(SOURCE_X, SOURCE_Y, DESTINATION_X, DESTINATION_Y)

        verify(mockObserver).onChanged(viewModel)
    }

    @Test
    fun shouldNotifyObserversWithViewModelThatContainsSolutions() {
        presenter.viewModelObservable().observeForever(mockObserver)
        val response = GameResponseBuilder()
            .withSolutions(SOLUTIONS)
            .build()
        val viewModel = GameViewModelBuilder()
            .withSolutions(FORMATTED_SOLUTION)
            .build()
        `when`(mockUseCase.execute(any())).thenReturn(response)

        presenter.onClick(SOURCE_X, SOURCE_Y, DESTINATION_X, DESTINATION_Y)

        verify(mockObserver).onChanged(viewModel)
    }
}