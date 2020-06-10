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
import constantine.theodoridis.android.game.chess.data.datasource.ResourceDataSource
import constantine.theodoridis.android.game.chess.data.repository.StringDepository
import constantine.theodoridis.android.game.chess.domain.usecase.ValidateMenuInputUseCase
import constantine.theodoridis.android.game.chess.presentation.mainmenu.builder.MainMenuViewModelBuilder
import constantine.theodoridis.android.game.chess.presentation.mainmenu.model.MainMenuViewModel
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit

@RunWith(JUnitParamsRunner::class)
class InputValidationTest {
    companion object {
        private const val BOARD_SIZE = "8"
        private const val MOVES = "3"
        private const val BOARD_SIZE_ERROR_MESSAGE = "Board size error message"
        private const val MOVES_ERROR_MESSAGE = "Moves error message"
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
    private lateinit var mockResourceDataSource: ResourceDataSource

    private lateinit var presenter: MainMenuPresenter

    @Before
    fun setUp() {
        val stringRepository = StringDepository(mockResourceDataSource)
        val useCase = ValidateMenuInputUseCase(stringRepository)
        presenter = MainMenuPresenter(useCase, Schedulers.trampoline())
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun cleanUp() {
        RxJavaPlugins.reset()
    }

    @Test
    fun shouldNotDisplayErrorMessages_whenInputIsValid() {
        presenter.viewModelObservable().observeForever(mockObserver)
        val viewModel = MainMenuViewModelBuilder().build()

        presenter.onStart(BOARD_SIZE, MOVES)

        verify(mockObserver).onChanged(viewModel)
    }

    @Test
    @Parameters(
        value = [
            "",
            "-",
            "5",
            "17"
        ]
    )
    fun shouldDisplayBoardSizeErrorMessage_whenBoardSizeIsInvalid(boardSize: String) {
        presenter.viewModelObservable().observeForever(mockObserver)
        val viewModel = MainMenuViewModelBuilder()
            .withBoardSizeError()
            .withBoardSizeErrorMessage(BOARD_SIZE_ERROR_MESSAGE)
            .build()
        `when`(mockResourceDataSource.getString(anyInt())).thenReturn(BOARD_SIZE_ERROR_MESSAGE)

        presenter.onStart(boardSize, MOVES)

        verify(mockObserver).onChanged(viewModel)
    }

    @Test
    @Parameters(
        value = [
            "",
            "-",
            "0"
        ]
    )
    fun shouldDisplayMovesErrorMessage_whenMovesInputIsInvalid(maxMoves: String) {
        presenter.viewModelObservable().observeForever(mockObserver)
        val viewModel = MainMenuViewModelBuilder()
            .withMovesError()
            .withMovesErrorMessage(MOVES_ERROR_MESSAGE)
            .build()
        `when`(mockResourceDataSource.getString(anyInt())).thenReturn(MOVES_ERROR_MESSAGE)

        presenter.onStart(BOARD_SIZE, maxMoves)

        verify(mockObserver).onChanged(viewModel)
    }
}