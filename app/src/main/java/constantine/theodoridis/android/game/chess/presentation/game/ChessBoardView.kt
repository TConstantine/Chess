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
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class ChessBoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var listener: OnTouchEventListener? = null
    private var size = 8
    private val outlinePaint = Paint()
    private val outline = Rect()
    private val tilePaint = Paint()
    private val tileOutlinePaint = Paint()
    private val tile = Rect()
    private var tileSize = 0
    private var sourceX = -1
    private var sourceY = -1
    private var destinationX = -1
    private var destinationY = -1

    override fun onDraw(canvas: Canvas?) {
        drawBoard(canvas)
        drawOutline(canvas)
        drawSource(canvas)
        drawDestination(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val width: Int
        val height: Int
        tileSize = calculateTileSize(widthSize, heightSize)
        val desiredWidth = tileSize * size
        val desiredHeight = tileSize * size
        width = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                widthSize
            }
            MeasureSpec.AT_MOST -> {
                min(desiredWidth, widthSize)
            }
            else -> {
                desiredWidth
            }
        }
        height = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                heightSize
            }
            MeasureSpec.AT_MOST -> {
                min(desiredHeight, heightSize)
            }
            else -> {
                desiredHeight
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val xCoordinate = event.x.toInt()
        val yCoordinate = event.y.toInt()
        if (event.action == MotionEvent.ACTION_DOWN) {
            // TODO: Keep x, y rectangle positions to avoid re-calculation
            for (column in 0 until size) {
                for (row in 0 until size) {
                    val x = calculateX(column)
                    val y = calculateY(row)
                    tile.set(x, y, x + tileSize, y + tileSize)
                    if (tile.contains(xCoordinate, yCoordinate)) {
                        listener!!.onTileClick(row, column)
                    }
                }
            }
            performClick()
            return true
        }
        return false
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun setSize(size: Int) {
        this.size = size
    }

    fun setSource(row: Int, column: Int) {
        sourceY = row
        sourceX = column
    }

    fun setDestination(row: Int, column: Int) {
        destinationY = row
        destinationX = column
    }

    fun setOnTouchEventListener(listener: OnTouchEventListener) {
        this.listener = listener
    }

    interface OnTouchEventListener {
        fun onTileClick(row: Int, column: Int)
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
        return tileSize * x
    }

    private fun calculateY(y: Int): Int {
        return tileSize * y
    }

    private fun isEven(row: Int, column: Int): Boolean {
        return (row + column) % 2 == 0
    }

    private fun drawBoard(canvas: Canvas?) {
        for (column in 0 until size) {
            for (row in 0 until size) {
                val x = calculateX(column)
                val y = calculateY(row)
                tile.set(x, y, x + tileSize, y + tileSize)
                tilePaint.color = if (isEven(row, column)) Color.BLACK else Color.WHITE
                canvas?.drawRect(tile, tilePaint)
            }
        }
    }

    private fun drawOutline(canvas: Canvas?) {
        outlinePaint.style = Paint.Style.STROKE
        outlinePaint.strokeWidth = 5.0F
        outline.set(0, 0, tileSize * size, tileSize * size)
        canvas?.drawRect(outline, outlinePaint)
    }

    private fun drawSource(canvas: Canvas?) {
        if (sourceX >= 0 && sourceY >= 0) {
            val x = calculateX(sourceX)
            val y = calculateY(sourceY)
            tile.set(x, y, x + tileSize, y + tileSize)
            tileOutlinePaint.color = Color.GREEN
            canvas?.drawRect(tile, tileOutlinePaint)
        }
    }

    private fun drawDestination(canvas: Canvas?) {
        if (destinationX >= 0 && destinationY >= 0) {
            val x = calculateX(destinationX)
            val y = calculateY(destinationY)
            tile.set(x, y, x + tileSize, y + tileSize)
            tileOutlinePaint.color = Color.RED
            canvas?.drawRect(tile, tileOutlinePaint)
        }
    }
}