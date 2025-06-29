package com.termux.snake

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var gameView: GameView
    private lateinit var gameOverLayout: LinearLayout
    private lateinit var scoreText: TextView
    private lateinit var finalScoreText: TextView
    private lateinit var highScoreText: TextView
    private lateinit var playAgainButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    
    companion object {
        private const val PREFS_NAME = "SnakeGamePrefs"
        private const val HIGH_SCORE_KEY = "HighScore"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        initSharedPreferences()
        setupGameCallbacks()
        startNewGame()
    }
    
    private fun initViews() {
        gameView = findViewById(R.id.gameView)
        gameOverLayout = findViewById(R.id.gameOverLayout)
        scoreText = findViewById(R.id.scoreText)
        finalScoreText = findViewById(R.id.finalScoreText)
        highScoreText = findViewById(R.id.highScoreText)
        playAgainButton = findViewById(R.id.playAgainButton)
        
        playAgainButton.setOnClickListener {
            startNewGame()
        }
    }
    
    private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    private fun setupGameCallbacks() {
        gameView.setGameCallbacks(object : GameView.GameCallbacks {
            override fun onScoreUpdate(score: Int) {
                runOnUiThread {
                    scoreText.text = getString(R.string.score, score)
                }
            }
            
            override fun onGameOver(finalScore: Int) {
                val highScore = getHighScore()
                if (finalScore > highScore) {
                    saveHighScore(finalScore)
                }
                
                runOnUiThread {
                    showGameOverScreen(finalScore, maxOf(finalScore, highScore))
                }
            }
        })
    }
    
    private fun startNewGame() {
        gameOverLayout.visibility = View.GONE
        gameView.startNewGame()
        updateScoreDisplay(0)
    }
    
    private fun showGameOverScreen(finalScore: Int, highScore: Int) {
        finalScoreText.text = getString(R.string.score, finalScore)
        highScoreText.text = getString(R.string.high_score, highScore)
        gameOverLayout.visibility = View.VISIBLE
    }
    
    private fun updateScoreDisplay(score: Int) {
        scoreText.text = getString(R.string.score, score)
    }
    
    private fun getHighScore(): Int {
        return sharedPreferences.getInt(HIGH_SCORE_KEY, 0)
    }
    
    private fun saveHighScore(score: Int) {
        sharedPreferences.edit()
            .putInt(HIGH_SCORE_KEY, score)
            .apply()
    }
    
    override fun onPause() {
        super.onPause()
        gameView.pauseGame()
    }
    
    override fun onResume() {
        super.onResume()
        gameView.resumeGame()
    }
}