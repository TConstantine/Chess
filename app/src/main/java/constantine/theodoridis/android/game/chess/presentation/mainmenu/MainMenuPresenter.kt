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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import constantine.theodoridis.android.game.chess.domain.UseCase
import constantine.theodoridis.android.game.chess.domain.request.MainMenuRequest
import constantine.theodoridis.android.game.chess.domain.response.MainMenuResponse
import constantine.theodoridis.android.game.chess.presentation.mainmenu.model.MainMenuViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainMenuPresenter(
    private val useCase: UseCase<MainMenuRequest, MainMenuResponse>,
    private val scheduler: Scheduler
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val viewModelObservable = MutableLiveData<MainMenuViewModel>()

    fun onStart(boardSize: Int, maxMoves: Int) {
        compositeDisposable.add(Single.fromCallable { useCase.execute(createRequest(boardSize, maxMoves)) }
            .subscribeOn(Schedulers.io())
            .observeOn(scheduler)
            .subscribeWith(object : DisposableSingleObserver<MainMenuResponse>() {
                override fun onSuccess(response: MainMenuResponse) {
                    viewModelObservable.postValue(createViewModel(response))
                }

                override fun onError(e: Throwable) {
                }
            })
        )
    }

    fun viewModelObservable(): LiveData<MainMenuViewModel> {
        return viewModelObservable
    }

    private fun createRequest(boardSize: Int, maxMoves: Int): MainMenuRequest {
        return MainMenuRequest(
            boardSize = boardSize,
            maxMoves = maxMoves
        )
    }

    private fun createViewModel(response: MainMenuResponse): MainMenuViewModel {
        return MainMenuViewModel(
            hasError = response.boardSizeErrorMessage != "" || response.maxMovesErrorMessage != "",
            boardSizeErrorMessage = response.boardSizeErrorMessage,
            maxMovesErrorMessage = response.maxMovesErrorMessage
        )
    }
}