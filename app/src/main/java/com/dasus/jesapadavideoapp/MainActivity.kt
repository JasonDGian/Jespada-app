package com.dasus.jesapadavideoapp

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    // Componentes visuales para las diferentes partes del logo.
    private lateinit var imagen_logo_3: ImageView
    private lateinit var imagen_logo_2: ImageView
    private lateinit var imagen_logo_21: ImageView
    private lateinit var imagen_logo_22: ImageView
    private lateinit var imagen_logo_1: ImageView

    // Animaciones asociadas al logo y los elementos visuales.
    private lateinit var animEspera: Animation
    private lateinit var animEspera2: Animation
    private lateinit var animJespada: Animation
    private lateinit var animEspada: Animation
    private lateinit var animEspadaLenta: Animation
    private lateinit var animEspadaLenta2: Animation
    private lateinit var animArriba: Animation

    // Reproductor multimedia para los efectos de sonido.
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configura ajustes de diseño para manejar bordes y márgenes con los insets del sistema.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa los componentes visuales.
        imagen_logo_3 = findViewById(R.id.logo_dimnamico_3)
        imagen_logo_2 = findViewById(R.id.logo_dimnamico_2)
        imagen_logo_21 = findViewById(R.id.logo_dimnamico_21)
        imagen_logo_22 = findViewById(R.id.logo_dimnamico_22)
        imagen_logo_1 = findViewById(R.id.logo_dimnamico_1)

        // Carga las animaciones desde los recursos.
        animEspera = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_espera)
        animEspera2 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_espera2)
        animJespada = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_jespada)
        animEspada = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_espada)
        animEspadaLenta = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_espada_lenta)
        animEspadaLenta2 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_espada_lenta2)
        animArriba = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.enviar_arriba)

        // Configura el reproductor de audio para los efectos de sonido de la espada.
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_espada2)

        // Inicia la animación de carga inicial para los elementos del logo.
        animacionEspera()

        // TODO: Cambiar este botón de prueba a uno en la barra de herramientas.
        val botonTest = findViewById<Button>(R.id.boton)
        botonTest.setOnClickListener {
            // Inicia la animación para mover todos los elementos del logo hacia arriba.
            imagen_logo_1.startAnimation(animArriba)
            imagen_logo_2.startAnimation(animArriba)
            imagen_logo_3.startAnimation(animArriba)
            imagen_logo_21.startAnimation(animArriba)
            imagen_logo_22.startAnimation(animArriba)
        }

        // Configura el intent para abrir la actividad de grabación.
        val intentoActividadGrabar = Intent(this, GrabarActivity::class.java)

        // Configura la acción al finalizar la animación "animArriba".
        animArriba.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Sin acción al inicio de esta animación.
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Oculta los elementos del logo al finalizar la animación.
                imagen_logo_1.isVisible = false
                imagen_logo_2.isVisible = false
                imagen_logo_3.isVisible = false
                imagen_logo_21.isVisible = false
                imagen_logo_22.isVisible = false
                // Navega a la actividad de grabación.
                startActivity(intentoActividadGrabar)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Esta animación no se repite.
            }
        })

        // Configura las acciones al finalizar la animación "animEspera".
        animEspera.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Reproduce el sonido de la espada cuando inicia esta animación.
                mediaPlayer.start()
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Inicia las siguientes animaciones para los elementos del logo.
                imagen_logo_2.startAnimation(animEspada)
                imagen_logo_21.startAnimation(animEspadaLenta)
                imagen_logo_22.startAnimation(animEspadaLenta2)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Esta animación no se repite.
            }
        })

        // Configura las acciones al finalizar la animación "animEspada".
        animEspada.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Esta animación es desencadenada por otra, no requiere acción al inicio.
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Inicia la animación "animJespada" en todos los elementos del logo.
                imagen_logo_3.startAnimation(animJespada)
                imagen_logo_1.startAnimation(animJespada)
                imagen_logo_2.startAnimation(animJespada)
                imagen_logo_21.startAnimation(animJespada)
                imagen_logo_22.startAnimation(animJespada)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Esta animación no se repite.
            }
        })

        // Permite reiniciar la animación al hacer clic en el logo principal.
        imagen_logo_1.setOnClickListener {
            animacionEspera()
        }
    }

    /**
     * Inicia la animación inicial que sirve como buffer para mostrar el logo.
     * Este retraso asegura que las animaciones principales se vean correctamente
     * incluso en dispositivos más lentos.
     */
    private fun animacionEspera() {
        imagen_logo_3.startAnimation(animEspera)
        imagen_logo_1.startAnimation(animEspera)
        imagen_logo_2.startAnimation(animEspera2)
        imagen_logo_21.startAnimation(animEspera2)
        imagen_logo_22.startAnimation(animEspera2)
    }
}
