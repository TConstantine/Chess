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

package constantine.theodoridis.android.game.chess.di

import constantine.theodoridis.android.game.chess.domain.entity.KnightPathsAlgorithm
import constantine.theodoridis.android.game.chess.domain.repository.KnightPathRepository
import constantine.theodoridis.android.game.chess.domain.repository.PreferenceRepository
import constantine.theodoridis.android.game.chess.domain.repository.StringRepository
import constantine.theodoridis.android.game.chess.domain.request.FindKnightPathsRequest
import constantine.theodoridis.android.game.chess.domain.request.LoadGameRequest
import constantine.theodoridis.android.game.chess.domain.response.FindKnightPathsResponse
import constantine.theodoridis.android.game.chess.domain.response.LoadGameResponse
import constantine.theodoridis.android.game.chess.domain.usecase.FindKnightPathsUseCase
import constantine.theodoridis.android.game.chess.domain.usecase.LoadGameUseCase
import constantine.theodoridis.android.game.chess.domain.usecase.UseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {
    @Provides
    fun provideLoadGameUseCase(
        preferenceRepository: PreferenceRepository,
        knightPathRepository: KnightPathRepository
    ): UseCase<LoadGameRequest, LoadGameResponse> {
        return LoadGameUseCase(preferenceRepository, knightPathRepository)
    }

    @Provides
    fun provideFindKnightPathsUseCase(
        preferenceRepository: PreferenceRepository,
        knightPathsAlgorithm: KnightPathsAlgorithm,
        stringRepository: StringRepository,
        knightPathRepository: KnightPathRepository
    ): UseCase<FindKnightPathsRequest, FindKnightPathsResponse> {
        return FindKnightPathsUseCase(
            preferenceRepository,
            knightPathsAlgorithm,
            stringRepository,
            knightPathRepository
        )
    }
}