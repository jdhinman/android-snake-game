package com.termux.snake

import android.graphics.Point
import kotlin.random.Random

class Food(private val boardWidth: Int, private val boardHeight: Int) {
    private var position = Point(0, 0)
    
    fun generateNew(snakeBody: List<Point>) {
        do {
            position = Point(
                Random.nextInt(boardWidth),
                Random.nextInt(boardHeight)
            )
        } while (snakeBody.any { it.x == position.x && it.y == position.y })
    }
    
    fun getPosition(): Point = position
    
    fun isEaten(snakeHead: Point): Boolean {
        return position.x == snakeHead.x && position.y == snakeHead.y
    }
}