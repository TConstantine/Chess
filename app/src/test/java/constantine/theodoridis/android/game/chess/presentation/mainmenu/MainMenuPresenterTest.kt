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

package constantine.theodoridis.android.game.chess.presentation.mainmenu

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import constantine.theodoridis.android.game.chess.domain.usecase.UseCase
import constantine.theodoridis.android.game.chess.domain.builder.MainMenuResponseBuilder
import constantine.theodoridis.android.game.chess.domain.request.MainMenuRequest
import constantine.theodoridis.android.game.chess.domain.response.MainMenuResponse
import constantine.theodoridis.android.game.chess.presentation.mainmenu.builder.MainMenuViewModelBuilder
import constantine.theodoridis.android.game.chess.presentation.mainmenu.model.MainMenuViewModel
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit

class MainMenuPresenterTest {
    companion object {
        private const val BOARD_SIZE = 8
        private const val INVALID_BOARD_SIZE = 0
        private const val MAX_MOVES = 3
        private const val INVALID_MAX_MOVES = 0
        private const val BOARD_SIZE_ERROR_MESSAGE = "Board size should be between 6 and 16"
        private const val MAX_MOVES_ERROR_MESSAGE = "Max moves should be greater than 0"
    }

    @Rule
    @JvmField
    val mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    val testExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockObserver: Observer<MainMenuViewModel>

    @Mock
    private lateinit var mockUseCase: UseCase<MainMenuRequest, MainMenuResponse>

    private lateinit var presenter: MainMenuPresenter

    @Before
    fun setUp() {
        presenter = MainMenuPresenter(mockUseCase, Schedulers.trampoline())
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun cleanUp() {
        RxJavaPlugins.reset()
    }

    @Test
    fun shouldDisplayErrorMessage_whenBoardSizeIsInvalid() {
        presenter.viewModelObservable().observeForever(mockObserver)
        val response = MainMenuResponseBuilder()
            .withBoardSizeErrorMessage(BOARD_SIZE_ERROR_MESSAGE)
            .build()
        val viewModel = MainMenuViewModelBuilder()
            .withError()
            .withBoardSizeErrorMessage(BOARD_SIZE_ERROR_MESSAGE)
            .build()
        `when`(mockUseCase.execute(any())).thenReturn(response)

        presenter.onStart(INVALID_BOARD_SIZE, MAX_MOVES)

        verify(mockObserver).onChanged(viewModel)
    }

    @Test
    fun shouldDisplayErrorMessage_whenMaxMovesAreInvalid() {
        presenter.viewModelObservable().observeForever(mockObserver)
        val response = MainMenuResponseBuilder()
            .withMaxMovesErrorMessage(MAX_MOVES_ERROR_MESSAGE)
            .build()
        val viewModel = MainMenuViewModelBuilder()
            .withError()
            .withMaxMovesErrorMessage(MAX_MOVES_ERROR_MESSAGE)
            .build()
        `when`(mockUseCase.execute(any())).thenReturn(response)

        presenter.onStart(BOARD_SIZE, INVALID_MAX_MOVES)

        verify(mockObserver).onChanged(viewModel)
    }
}