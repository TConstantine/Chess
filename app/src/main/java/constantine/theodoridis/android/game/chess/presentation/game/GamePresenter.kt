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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import constantine.theodoridis.android.game.chess.domain.entity.KnightPath
import constantine.theodoridis.android.game.chess.domain.request.GameRequest
import constantine.theodoridis.android.game.chess.domain.response.GameResponse
import constantine.theodoridis.android.game.chess.domain.usecase.UseCase
import constantine.theodoridis.android.game.chess.presentation.game.model.GameViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class GamePresenter(
    private val useCase: UseCase<GameRequest, GameResponse>,
    private val scheduler: Scheduler
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val viewModelObservable = MutableLiveData<GameViewModel>()

    fun onClick(sourceX: Int, sourceY: Int, destinationX: Int, destinationY: Int) {
        val request = createRequest(sourceX, sourceY, destinationX, destinationY)
        compositeDisposable.add(Single.fromCallable { useCase.execute(request) }
            .subscribeOn(Schedulers.io())
            .observeOn(scheduler)
            .subscribeWith(object : DisposableSingleObserver<GameResponse>() {
                override fun onSuccess(response: GameResponse) {
                    viewModelObservable.postValue(createViewModel(response))
                }

                override fun onError(e: Throwable) {
                }
            })
        )
    }

    fun viewModelObservable(): LiveData<GameViewModel> {
        return viewModelObservable
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

    private fun createRequest(
        sourceX: Int,
        sourceY: Int,
        destinationX: Int,
        destinationY: Int
    ): GameRequest {
        return GameRequest(
            sourceX = sourceX,
            sourceY = sourceY,
            destinationX = destinationX,
            destinationY = destinationY
        )
    }

    private fun createViewModel(response: GameResponse): GameViewModel {
        return GameViewModel(
            solutions = if (response.solutionErrorMessage == "") {
                formatSolutions(response.solutions)
            } else {
                response.solutionErrorMessage
            }
        )
    }

    private fun formatSolutions(solutions: List<KnightPath>): String {
        var formattedSolution = ""
        for (i in solutions.indices) {
            formattedSolution += "${i + 1}. "
            val knightPath = solutions[i].path
            for (j in knightPath.indices) {
                formattedSolution += knightPath[j]
                if (j != knightPath.size - 1) {
                    formattedSolution += " -> "
                }
            }
            if (i != solutions.size - 1) {
                formattedSolution += "\n"
            }
        }
        return formattedSolution
    }
}