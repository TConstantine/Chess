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
import constantine.theodoridis.android.game.chess.domain.request.FindKnightPathsRequest
import constantine.theodoridis.android.game.chess.domain.request.LoadGameRequest
import constantine.theodoridis.android.game.chess.domain.response.FindKnightPathsResponse
import constantine.theodoridis.android.game.chess.domain.response.LoadGameResponse
import constantine.theodoridis.android.game.chess.domain.usecase.UseCase
import constantine.theodoridis.android.game.chess.presentation.game.model.FindKnightPathsViewModel
import constantine.theodoridis.android.game.chess.presentation.game.model.LoadGameViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class GamePresenter(
    private val loadGameUseCase: UseCase<LoadGameRequest, LoadGameResponse>,
    private val findKnightPathsUseCase: UseCase<FindKnightPathsRequest, FindKnightPathsResponse>,
    private val scheduler: Scheduler
) : ViewModel() {
    companion object {
        private const val EMPTY_STRING = ""
    }

    private val compositeDisposable = CompositeDisposable()
    private val loadGameObservable = MutableLiveData<LoadGameViewModel>()
    private val findKnightPathsObservable = MutableLiveData<FindKnightPathsViewModel>()

    fun onLoad() {
        compositeDisposable.add(Single.fromCallable { loadGameUseCase.execute(LoadGameRequest()) }
            .subscribeOn(Schedulers.io())
            .observeOn(scheduler)
            .subscribeWith(object : DisposableSingleObserver<LoadGameResponse>() {
                override fun onSuccess(response: LoadGameResponse) {
                    loadGameObservable.postValue(createViewModelFromLoadGameResponse(response))
                }

                override fun onError(e: Throwable) {
                }
            })
        )
    }

    fun onBoardClick(sourceX: Int, sourceY: Int, destinationX: Int, destinationY: Int) {
        val request = createFindKnightPathsRequest(sourceX, sourceY, destinationX, destinationY)
        compositeDisposable.add(Single.fromCallable { findKnightPathsUseCase.execute(request) }
            .subscribeOn(Schedulers.io())
            .observeOn(scheduler)
            .subscribeWith(object : DisposableSingleObserver<FindKnightPathsResponse>() {
                override fun onSuccess(response: FindKnightPathsResponse) {
                    findKnightPathsObservable.postValue(
                        createViewModelFromFindKnightPathsResponse(
                            response
                        )
                    )
                }

                override fun onError(e: Throwable) {
                }
            })
        )
    }

    fun loadGameViewModelObservable(): LiveData<LoadGameViewModel> {
        return loadGameObservable
    }

    fun findKnightPathsViewModelObservable(): LiveData<FindKnightPathsViewModel> {
        return findKnightPathsObservable
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

    private fun createFindKnightPathsRequest(
        sourceX: Int,
        sourceY: Int,
        destinationX: Int,
        destinationY: Int
    ): FindKnightPathsRequest {
        return FindKnightPathsRequest(
            sourceX = sourceX,
            sourceY = sourceY,
            destinationX = destinationX,
            destinationY = destinationY
        )
    }

    private fun createViewModelFromLoadGameResponse(response: LoadGameResponse): LoadGameViewModel {
        return LoadGameViewModel(
            boardSize = response.boardSize,
            solutions = if (response.solutions.isEmpty()) {
                EMPTY_STRING
            } else {
                formatSolutions(response.solutions)
            }
        )
    }

    private fun createViewModelFromFindKnightPathsResponse(response: FindKnightPathsResponse): FindKnightPathsViewModel {
        return FindKnightPathsViewModel(
            solutions = if (response.solutionErrorMessage == EMPTY_STRING) {
                formatSolutions(response.solutions)
            } else {
                response.solutionErrorMessage
            }
        )
    }

    private fun formatSolutions(solutions: List<KnightPath>): String {
        var formattedSolution = EMPTY_STRING
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