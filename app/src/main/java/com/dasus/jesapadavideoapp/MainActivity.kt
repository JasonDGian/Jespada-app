package com.dasus.jesapadavideoapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.res.ResourcesCompat
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
    private lateinit var animArriba2: Animation


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

        // Configurar la Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val iconoToolbar = AppCompatResources.getDrawable(this, R.drawable.menu_icon)
        toolbar.overflowIcon = iconoToolbar

        // Inicializa los componentes visuales.
        imagen_logo_3 = findViewById(R.id.logo_dinamico_3)
        imagen_logo_2 = findViewById(R.id.logo_dinamico_2)
        imagen_logo_21 = findViewById(R.id.logo_dinamico_2_1)
        imagen_logo_22 = findViewById(R.id.logo_dinamico_2_2)
        imagen_logo_1 = findViewById(R.id.logo_dinamico_1)

        var logoMenu = findViewById<ImageView>(R.id.toolbar_logo)
        logoMenu.isVisible = false

        // Carga las animaciones desde los recursos.
        animEspera = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_espera)
        animEspera2 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_espera2)
        animJespada = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_jespada)
        animEspada = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_espada)
        animEspadaLenta = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_espada_lenta)
        animEspadaLenta2 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_espada_lenta2)
        animArriba = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.enviar_arriba)
        animArriba2 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.enviar_arriba)
        var espadaAparecer = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.logo_espada_aparecer)

        // Configura el reproductor de audio para los efectos de sonido de la espada.
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_espada2)



        // Inicia la animación de carga inicial para los elementos del logo.
        animacionEspera()

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
                logoMenu.startAnimation(espadaAparecer)
                logoMenu.isVisible = true

                // Navega a la actividad de grabación.
                startActivity(intentoActividadGrabar)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Esta animación no se repite.
            }
        })

        // Configura el intent para abrir la actividad de grabación.
        val intentoActividadReproducir = Intent(this, ReproducirActivity::class.java)

        // Configura la acción al finalizar la animación "animArriba".
        animArriba2.setAnimationListener(object : Animation.AnimationListener {
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
                logoMenu.startAnimation(espadaAparecer)
                logoMenu.isVisible = true

                // Navega a la actividad de grabación.
                startActivity(intentoActividadReproducir)
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


    // Funcion que "infla" el boton del menu con las opciones pasadas por parametro.
    // En este caso infla con las funciones especificas de la pantalla principal para acceder
    // a las pantallas de grabar o reproducir.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Infla el menú; agrega elementos al Toolbar
        menuInflater.inflate(R.menu.opciones_menu, menu)
        return true
    }

    // Funcion que según el elemento seleccionado en el menú llevará a una pantalla
    // o a otra.
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.opcion_grabar -> {

            // Inicia la animación para mover todos los elementos del logo hacia arriba.
            imagen_logo_1.startAnimation(animArriba)
            imagen_logo_2.startAnimation(animArriba)
            imagen_logo_3.startAnimation(animArriba)
            imagen_logo_21.startAnimation(animArriba)
            imagen_logo_22.startAnimation(animArriba)

            // True para indicar que la opción ha sido "manejada"
            true
        }
        R.id.opcion_reproducir -> {
            // Lanza la pantalla de grabar.
            imagen_logo_1.startAnimation(animArriba2)
            imagen_logo_2.startAnimation(animArriba2)
            imagen_logo_3.startAnimation(animArriba2)
            imagen_logo_21.startAnimation(animArriba2)
            imagen_logo_22.startAnimation(animArriba2)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }


}
