package com.dasus.jesapadavideoapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class ReproducirActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reproducir)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar la Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_reproducir)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val iconoToolbar = AppCompatResources.getDrawable(this, R.drawable.menu_icon)
        toolbar.overflowIcon = iconoToolbar

        fun obtenerUriVideoGuardado(): Uri? {
            // Define la ruta del directorio donde el video esta almacenado
            val videoDirectory = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                "Jespada"
            )

            // Verifica si el video existe
            val videoFile = File(videoDirectory, "Video.mp4")
            return if (videoFile.exists()) {
                // Usa Uri.fromFile para obtener la Uri del archivo
                Uri.fromFile(videoFile)
            } else {
                // Retorna null si el archivo no existe
                null
            }
        }

        val video = findViewById<VideoView>(R.id.video)
        val cardReproducir = findViewById<CardView>(R.id.cardReproducir)
        val cardPausa = findViewById<CardView>(R.id.cardPausar)
        val tvNoVideo = findViewById<TextView>(R.id.videoNoEncontrado)
        val ivReproducir= findViewById<ImageView>(R.id.ivStart)
        val ivPausa= findViewById<ImageView>(R.id.ivPause)

        val videoUri = obtenerUriVideoGuardado()
        // vemos si el video existe
        if (videoUri != null) {
            // Como el video existe mostramos el VideoView y ocultamos el texto
            video.setVideoURI(videoUri)
            video.visibility = VideoView.VISIBLE
            tvNoVideo.visibility = TextView.GONE
            cardReproducir.isEnabled = true
            cardPausa.isEnabled = false
            cardPausa.setCardBackgroundColor(getColor(R.color.azuldisabled))
            ivPausa.setImageResource(R.mipmap.boton_pausar_disabled)
        } else {
            // como no hay video mostramos el mensaje y ocultamos el VideoView
            video.visibility = VideoView.GONE
            tvNoVideo.visibility = TextView.VISIBLE
            cardReproducir.isEnabled = false
            cardPausa.isEnabled = false
            cardReproducir.setCardBackgroundColor(getColor(R.color.azuldisabled))
            cardPausa.setCardBackgroundColor(getColor(R.color.azuldisabled))
            ivPausa.setImageResource(R.mipmap.boton_pausar_disabled)
            ivReproducir.setImageResource(R.mipmap.boton_reproducir_disabled)
        }

        // Configurar botones
        cardReproducir.setOnClickListener {
            if (!video.isPlaying) {
                video.start()
                cardReproducir.isEnabled = false
                cardPausa.isEnabled = true
                cardReproducir.setCardBackgroundColor(getColor(R.color.azuldisabled))
                cardPausa.setCardBackgroundColor(getColor(R.color.azulclaromenu))
                ivPausa.setImageResource(R.mipmap.boton_pause)
                ivReproducir.setImageResource(R.mipmap.boton_reproducir_disabled)
            }
        }

        cardPausa.setOnClickListener {
            if (video.isPlaying) {
                video.pause()
                cardReproducir.isEnabled = true
                cardPausa.isEnabled = false
                cardReproducir.setCardBackgroundColor(getColor(R.color.azulclaromenu))
                cardPausa.setCardBackgroundColor(getColor(R.color.azuldisabled))
                ivPausa.setImageResource(R.mipmap.boton_pausar_disabled)
                ivReproducir.setImageResource(R.mipmap.boton_play)
            }
        }
        video.setOnCompletionListener {
            cardReproducir.isEnabled = true
            cardPausa.isEnabled = false
            cardReproducir.setCardBackgroundColor(getColor(R.color.azulclaromenu))
            cardPausa.setCardBackgroundColor(getColor(R.color.azuldisabled))
            ivPausa.setImageResource(R.mipmap.boton_pausar_disabled)
            ivReproducir.setImageResource(R.mipmap.boton_play)
        }

    }

    // Funcion que "infla" el boton del menu con las opciones pasadas por parametro.
    // En este caso infla con las funciones especificas de la pantalla principal para acceder
    // a las pantallas de grabar o reproducir.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Infla el menú; agrega elementos al Toolbar
        menuInflater.inflate(R.menu.opciones_menu_reproducir, menu)
        return true
    }


    // Funcion que según el elemento seleccionado en el menú llevará a una pantalla
    // o a otra.
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.opcion_grabar -> {

            // Lanza la pantalla de grabar.
            startActivity(Intent(this, GrabarActivity::class.java))

            // True para indicar que la opción ha sido "manejada"
            true
        }
        R.id.opcion_volver -> {
            // Lanza la pantalla de grabar.
            startActivity(Intent(this, MainActivity::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

}