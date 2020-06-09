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

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class ChessBoardView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private var size = 0
    private val tilePaint = Paint()
    private val tile = Rect()
    private var tileSize = 0
    private var xOrigin = 0
    private var yOrigin = 0

    override fun onDraw(canvas: Canvas?) {
        tileSize = calculateTileSize(width, height)
        calculateOrigins(width, height)
        for (row in 0 until size) {
            for (column in 0 until size) {
                val x = calculateX(row)
                val y = calculateY(column)
                tile.set(x, y, x + tileSize, y + tileSize)
                tilePaint.color = if(isEven(row, column)) Color.BLACK else Color.WHITE
                canvas?.drawRect(tile, tilePaint)
            }
        }
    }

    fun setSize(size: Int) {
        this.size = size
    }

    private fun calculateTileSize(width: Int, height: Int): Int {
        return min(calculateTileWidth(width), calculateTileHeight(height))
    }
    private fun calculateTileWidth(width: Int): Int {
        return width / size
    }

    private fun calculateTileHeight(height: Int): Int {
        return height / size
    }

    private fun calculateX(x: Int): Int {
        return xOrigin + tileSize * x
    }

    private fun calculateY(y: Int): Int {
        return yOrigin + tileSize * (size - 1 - y)
    }

    private fun calculateOrigins(width: Int, height: Int) {
        xOrigin = (width - tileSize * size) / 2
        yOrigin = (height - tileSize * size) / 2
    }

    private fun isEven(row: Int, column: Int): Boolean {
        return (row + column) % 2 == 0
    }
}