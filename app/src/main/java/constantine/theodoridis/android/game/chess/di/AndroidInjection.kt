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

import constantine.theodoridis.android.game.chess.presentation.game.GameFragment

class AndroidInjection {
	companion object {
		fun inject(target: GameFragment) {
			DaggerFragmentComponent.builder()
				.activityModule(ActivityModule(target.requireContext()))
                .schedulerModule(SchedulerModule())
                .presenterModule(PresenterModule())
                .useCaseModule(UseCaseModule())
                .entityModule(EntityModule())
				.repositoryModule(RepositoryModule())
				.dataSourceModule(DataSourceModule())
				.build()
				.inject(target)
		}
	}
}