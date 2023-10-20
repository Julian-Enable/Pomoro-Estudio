package com.example.pomodoro

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Inicializa el reproductor de medios
        mediaPlayer = MediaPlayer.create(this, R.raw.welcome_sound)

        // Configura un oyente para detectar cuándo se ha completado la reproducción del sonido
        mediaPlayer.setOnCompletionListener { mp ->
            // Cuando se completa la reproducción, detiene y libera el reproductor de medios
            mp.stop()
            mp.release()
        }

        // Inicializa el botón
        val startButton = findViewById<Button>(R.id.startButton)

        // Configura un oyente para el clic en el botón
        startButton.setOnClickListener {
            // Reproduce el sonido de bienvenida
            mediaPlayer.start()

            // Espera a que se complete la reproducción del sonido antes de abrir la actividad principal
            mediaPlayer.setOnCompletionListener { mp ->
                // Cuando se completa la reproducción, detiene y libera el reproductor de medios
                mp.stop()
                mp.release()

                // Inicia la actividad principal
                val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Asegúrate de liberar el recurso de medios cuando se destruye la actividad
    }
}
