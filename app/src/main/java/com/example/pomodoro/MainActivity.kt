package com.example.pomodoro

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private var timeLeftInMillis: Long = 1500000 // 25 minutos en milisegundos
    private var timerRunning: Boolean = false
    private lateinit var countDownTimer: CountDownTimer
    private var pomodoroCount: Int = 0
    private var isResting: Boolean = false
    private var restTimeInMillis: Long = 300000 // 5 minutos de descanso
    private var longRestTimeInMillis: Long = 1800000 // 30 minutos de descanso largo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timerTextView: TextView = findViewById(R.id.timerTextView)
        val messageTextView: TextView = findViewById(R.id.messageTextView)
        val startButton: Button = findViewById(R.id.startButton)
        val pauseButton: Button = findViewById(R.id.pauseButton)
        val resetButton: Button = findViewById(R.id.resetButton)

        startButton.setOnClickListener {
            startTimer()
        }

        pauseButton.setOnClickListener {
            pauseTimer()
        }

        resetButton.setOnClickListener {
            resetTimer()
        }

        updateTimerText(timerTextView)
        messageTextView.text = "¡Dale Iniciar Cuando estes listo\npara estudiar!"
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText(findViewById(R.id.timerTextView))
                updateMessageTextView()
            }

            override fun onFinish() {
                timerRunning = false
                pomodoroCount++

                if (pomodoroCount < 4) {
                    // Realizar un descanso corto de 5 minutos
                    startRestTimer(restTimeInMillis)
                } else {
                    // Realizar un descanso largo de 15-30 minutos después de cuatro pomodoros
                    val randomRestTime = (1500000..1800000).random()
                    startRestTimer(randomRestTime.toLong())
                    pomodoroCount = 0 // Reiniciar el contador de pomodoros
                }
            }
        }.start()

        timerRunning = true
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        timerRunning = false
    }

    private fun resetTimer() {
        timeLeftInMillis = 1500000 // 25 minutos en milisegundos
        isResting = false // Asegurarse de que no esté en modo de descanso
        pomodoroCount = 0 // Reiniciar el contador de pomodoros si se reinicia durante un pomodoro
        updateTimerText(findViewById(R.id.timerTextView))
        updateMessageTextView()
    }

    private fun updateTimerText(timerTextView: TextView) {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60

        val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        timerTextView.text = timeFormatted
    }

    private fun startRestTimer(restTime: Long) {
        isResting = true // Establecer el estado a descanso
        countDownTimer = object : CountDownTimer(restTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText(findViewById(R.id.timerTextView))
                updateMessageTextView()
            }

            override fun onFinish() {
                timerRunning = false
                // Iniciar un nuevo pomodoro después del descanso
                startTimer()
                isResting = false
            }
        }.start()

        isResting = true
    }

    private fun updateMessageTextView() {
        val messageTextView: TextView = findViewById(R.id.messageTextView)

        if (isResting) {
            messageTextView.text = "Es tiempo de descanso"
            messageTextView.setTextColor(resources.getColor(R.color.colorRest))
        } else {
            messageTextView.text = "Es tiempo de estudio"
            messageTextView.setTextColor(resources.getColor(R.color.colorStudy))
        }
    }
}
