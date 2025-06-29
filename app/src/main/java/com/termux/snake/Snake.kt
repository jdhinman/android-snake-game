package com.termux.snake

import android.graphics.Point

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

class Snake(startX: Int, startY: Int) {
    private val body = mutableListOf<Point>()
    var direction = Direction.RIGHT
        private set
    private var nextDirection = Direction.RIGHT
    
    init {
        // Initialize snake with 3 segments
        body.add(Point(startX, startY))
        body.add(Point(startX - 1, startY))
        body.add(Point(startX - 2, startY))
    }
    
    fun getBody(): List<Point> = body.toList()
    
    fun getHead(): Point = body[0]
    
    fun setDirection(newDirection: Direction) {
        // Prevent immediate reverse direction
        when (direction) {
            Direction.UP -> if (newDirection != Direction.DOWN) nextDirection = newDirection
            Direction.DOWN -> if (newDirection != Direction.UP) nextDirection = newDirection
            Direction.LEFT -> if (newDirection != Direction.RIGHT) nextDirection = newDirection
            Direction.RIGHT -> if (newDirection != Direction.LEFT) nextDirection = newDirection
        }
    }
    
    fun move(): Point {
        // Update direction
        direction = nextDirection
        
        // Calculate new head position
        val head = body[0]
        val newHead = when (direction) {
            Direction.UP -> Point(head.x, head.y - 1)
            Direction.DOWN -> Point(head.x, head.y + 1)
            Direction.LEFT -> Point(head.x - 1, head.y)
            Direction.RIGHT -> Point(head.x + 1, head.y)
        }
        
        // Add new head
        body.add(0, newHead)
        
        // Return and remove tail (will be added back if food eaten)
        return body.removeAt(body.size - 1)
    }
    
    fun grow() {
        // Add a segment at the tail position
        val tail = body[body.size - 1]
        val secondToLast = if (body.size > 1) body[body.size - 2] else tail
        
        val newTail = when {
            tail.x < secondToLast.x -> Point(tail.x - 1, tail.y)
            tail.x > secondToLast.x -> Point(tail.x + 1, tail.y)
            tail.y < secondToLast.y -> Point(tail.x, tail.y - 1)
            tail.y > secondToLast.y -> Point(tail.x, tail.y + 1)
            else -> Point(tail.x - 1, tail.y) // Default case
        }
        
        body.add(newTail)
    }
    
    fun checkSelfCollision(): Boolean {
        val head = body[0]
        return body.drop(1).any { it.x == head.x && it.y == head.y }
    }
    
    fun reset(startX: Int, startY: Int) {
        body.clear()
        body.add(Point(startX, startY))
        body.add(Point(startX - 1, startY))
        body.add(Point(startX - 2, startY))
        direction = Direction.RIGHT
        nextDirection = Direction.RIGHT
    }
    
    fun getLength(): Int = body.size
}