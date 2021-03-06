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

package constantine.theodoridis.android.game.chess.data.datasource

import android.content.res.Resources
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit

class ResourcesDataSourceTest {
    companion object {
        private const val RESOURCE_ID = 0
        private const val EMPTY_STRING = ""
    }

    @Rule
    @JvmField
    val mockitoRule = MockitoJUnit.rule()!!

    @Mock
    private lateinit var mockResources: Resources

    private lateinit var resourceDataSource: ResourceDataSource

    @Before
    fun setUp() {
        resourceDataSource = ResourcesDataSource(mockResources)
    }

    @Test
    fun shouldReturnIntResourceFromResources() {
        resourceDataSource.getInteger(RESOURCE_ID)

        verify(mockResources).getInteger(RESOURCE_ID)
    }

    @Test
    fun shouldReturnStringResourceFromResources() {
        `when`(mockResources.getString(anyInt())).thenReturn(EMPTY_STRING)

        resourceDataSource.getString(RESOURCE_ID)

        verify(mockResources).getString(RESOURCE_ID)
    }
}