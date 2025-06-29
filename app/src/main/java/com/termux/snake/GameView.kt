package com.termux.snake

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.abs

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val BOARD_WIDTH = 20
        private const val BOARD_HEIGHT = 30
        private const val GAME_SPEED = 200L // milliseconds
    }

    private val snake = Snake(BOARD_WIDTH / 2, BOARD_HEIGHT / 2)
    private val food = Food(BOARD_WIDTH, BOARD_HEIGHT)
    private var score = 0
    private var isGameRunning = false
    private var isPaused = false

    private val handler = Handler(Looper.getMainLooper())
    private val gameRunnable = object : Runnable {
        override fun run() {
            if (isGameRunning && !isPaused) {
                updateGame()
                invalidate()
                handler.postDelayed(this, GAME_SPEED)
            }
        }
    }

    // Touch handling
    private var touchStartX = 0f
    private var touchStartY = 0f
    private val minSwipeDistance = 100f

    // Paint objects
    private val snakePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.snake_green)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val snakeHeadPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.snake_green)
        style = Paint.Style.FILL
        isAntiAlias = true
        alpha = 255
    }

    private val foodPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.food_red)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val backgroundPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.game_board)
        style = Paint.Style.FILL
    }

    private val gridPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.background_dark)
        style = Paint.Style.STROKE
        strokeWidth = 2f
        alpha = 100
    }

    // Cell dimensions (calculated in onSizeChanged)
    private var cellWidth = 0f
    private var cellHeight = 0f

    // Callbacks
    interface GameCallbacks {
        fun onScoreUpdate(score: Int)
        fun onGameOver(finalScore: Int)
    }

    private var gameCallbacks: GameCallbacks? = null

    fun setGameCallbacks(callbacks: GameCallbacks) {
        this.gameCallbacks = callbacks
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cellWidth = w.toFloat() / BOARD_WIDTH
        cellHeight = h.toFloat() / BOARD_HEIGHT
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw background
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        // Draw grid lines
        drawGrid(canvas)

        // Draw food
        drawFood(canvas)

        // Draw snake
        drawSnake(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        // Vertical lines
        for (i in 0..BOARD_WIDTH) {
            val x = i * cellWidth
            canvas.drawLine(x, 0f, x, height.toFloat(), gridPaint)
        }

        // Horizontal lines
        for (i in 0..BOARD_HEIGHT) {
            val y = i * cellHeight
            canvas.drawLine(0f, y, width.toFloat(), y, gridPaint)
        }
    }

    private fun drawFood(canvas: Canvas) {
        val foodPos = food.getPosition()
        val left = foodPos.x * cellWidth + cellWidth * 0.1f
        val top = foodPos.y * cellHeight + cellHeight * 0.1f
        val right = (foodPos.x + 1) * cellWidth - cellWidth * 0.1f
        val bottom = (foodPos.y + 1) * cellHeight - cellHeight * 0.1f

        canvas.drawOval(left, top, right, bottom, foodPaint)
    }

    private fun drawSnake(canvas: Canvas) {
        val snakeBody = snake.getBody()
        
        snakeBody.forEachIndexed { index, segment ->
            val left = segment.x * cellWidth + cellWidth * 0.05f
            val top = segment.y * cellHeight + cellHeight * 0.05f
            val right = (segment.x + 1) * cellWidth - cellWidth * 0.05f
            val bottom = (segment.y + 1) * cellHeight - cellHeight * 0.05f

            val paint = if (index == 0) snakeHeadPaint else snakePaint
            canvas.drawRoundRect(left, top, right, bottom, 
                cellWidth * 0.2f, cellHeight * 0.2f, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStartX = event.x
                touchStartY = event.y
                return true
            }
            MotionEvent.ACTION_UP -> {
                val deltaX = event.x - touchStartX
                val deltaY = event.y - touchStartY
                
                if (abs(deltaX) > minSwipeDistance || abs(deltaY) > minSwipeDistance) {
                    if (abs(deltaX) > abs(deltaY)) {
                        // Horizontal swipe
                        if (deltaX > 0) {
                            snake.setDirection(Direction.RIGHT)
                        } else {
                            snake.setDirection(Direction.LEFT)
                        }
                    } else {
                        // Vertical swipe
                        if (deltaY > 0) {
                            snake.setDirection(Direction.DOWN)
                        } else {
                            snake.setDirection(Direction.UP)
                        }
                    }
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun updateGame() {
        if (!isGameRunning) return

        // Move snake
        val removedTail = snake.move()

        // Check wall collision
        val head = snake.getHead()
        if (head.x < 0 || head.x >= BOARD_WIDTH || 
            head.y < 0 || head.y >= BOARD_HEIGHT) {
            gameOver()
            return
        }

        // Check self collision
        if (snake.checkSelfCollision()) {
            gameOver()
            return
        }

        // Check food collision
        if (food.isEaten(head)) {
            snake.grow()
            score += 10
            food.generateNew(snake.getBody())
            gameCallbacks?.onScoreUpdate(score)
        }
    }

    fun startNewGame() {
        snake.reset(BOARD_WIDTH / 2, BOARD_HEIGHT / 2)
        food.generateNew(snake.getBody())
        score = 0
        isGameRunning = true
        isPaused = false
        gameCallbacks?.onScoreUpdate(score)
        handler.post(gameRunnable)
    }

    fun pauseGame() {
        isPaused = true
    }

    fun resumeGame() {
        if (isGameRunning) {
            isPaused = false
            handler.post(gameRunnable)
        }
    }

    private fun gameOver() {
        isGameRunning = false
        handler.removeCallbacks(gameRunnable)
        gameCallbacks?.onGameOver(score)
    }
}