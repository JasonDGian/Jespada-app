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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mediaPlayer = MediaPlayer.create(this, R.raw.sonido_espada2);

        // Recupera elementos que componen el logo.
        var imagen_logo_3 = findViewById<ImageView>(R.id.logo_dimnamico_3)
        var imagen_logo_2 = findViewById<ImageView>(R.id.logo_dimnamico_2)
        var imagen_logo_21 = findViewById<ImageView>(R.id.logo_dimnamico_21)
        var imagen_logo_22 = findViewById<ImageView>(R.id.logo_dimnamico_22)
        var imagen_logo_1 = findViewById<ImageView>(R.id.logo_dimnamico_1)

        // Declarar el recurso de animación (especificando que es de android y no la de google)
        val animEspera = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_espera)
        val animEspera2 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_espera2)
        val animJespada = android.view.animation.AnimationUtils.loadAnimation( this, R.anim.anim_jespada );
        val animEspada = android.view.animation.AnimationUtils.loadAnimation( this, R.anim.anim_espada );
        val animEspadaLenta = android.view.animation.AnimationUtils.loadAnimation( this, R.anim.anim_espada_lenta );
        val animEspadaLenta2 = android.view.animation.AnimationUtils.loadAnimation( this, R.anim.anim_espada_lenta2 );
        val animArriba = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.enviar_arriba)

//        val animBoom = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_boom)
//        val animBoom2 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_boom2)
//        val animBoom3 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_boom3)
//        val animBoom4 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_boom4)
//        val animBoom5 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_boom5)

        imagen_logo_3.startAnimation(animEspera)
        imagen_logo_1.startAnimation(animEspera)
        imagen_logo_2.startAnimation(animEspera2)
        imagen_logo_21.startAnimation(animEspera2)
        imagen_logo_22.startAnimation(animEspera2)


        var botonTest = findViewById<Button>(R.id.boton)
        val intentoActividadGrabar = Intent( this, GrabarActivity::class.java )


        animArriba.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Start new animations when the first animation ends
                imagen_logo_1.isVisible = false
                imagen_logo_2.isVisible = false
                imagen_logo_3.isVisible = false
                imagen_logo_21.isVisible = false
                imagen_logo_22.isVisible = false
                startActivity(intentoActividadGrabar)
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })

        botonTest.setOnClickListener {
            imagen_logo_1.startAnimation(animArriba)
            imagen_logo_2.startAnimation(animArriba)
            imagen_logo_3.startAnimation(animArriba)
            imagen_logo_21.startAnimation(animArriba)
            imagen_logo_22.startAnimation(animArriba)
        }


        // Configuración de escucha para que cuando acabe de aparecer el logo
        animEspera.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // La animación la inicia otro elemento.
                mediaPlayer.start()
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Start new animations when the first animation ends
                imagen_logo_2.startAnimation(animEspada)
                imagen_logo_21.startAnimation(animEspadaLenta)
                imagen_logo_22.startAnimation(animEspadaLenta2)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                //  No se repite la animación.

            }
        })



        animEspada.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // La animación la inicia otro listener.
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Start new animations when the first animation ends
                imagen_logo_3.startAnimation(animJespada)
                imagen_logo_1.startAnimation(animJespada)
                imagen_logo_2.startAnimation(animJespada)
                imagen_logo_21.startAnimation(animJespada)
                imagen_logo_22.startAnimation(animJespada)

            }

            override fun onAnimationRepeat(animation: Animation?) {
                //  No se repite la animación.
            }
        })

//        animBoom.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(animation: Animation?) {
//                // La animación la inicia otro listener.
//            }
//
//            override fun onAnimationEnd(animation: Animation?) {
//                // Start new animations when the first animation ends
//                imagen_logo_1.isVisible = false
//                imagen_logo_2.isVisible = false
//                imagen_logo_3.isVisible = false
//                imagen_logo_21.isVisible = false
//                imagen_logo_22.isVisible = false
//            }
//
//            override fun onAnimationRepeat(animation: Animation?) {
//                //  No se repite la animación.
//            }
//        })

        var contadorClicks = 0
        imagen_logo_1.setOnClickListener(){
            contadorClicks++
            if ( contadorClicks > 10 ){
//                imagen_logo_3.startAnimation(animBoom)
//                imagen_logo_1.startAnimation(animBoom2)
//                imagen_logo_2.startAnimation(animBoom3)
//                imagen_logo_21.startAnimation(animBoom4)
//                imagen_logo_22.startAnimation(animBoom5)
                contadorClicks=0
            }
            else {
                imagen_logo_3.startAnimation(animEspera)
                imagen_logo_1.startAnimation(animEspera)
                imagen_logo_2.startAnimation(animEspera2)
                imagen_logo_21.startAnimation(animEspera2)
                imagen_logo_22.startAnimation(animEspera2)
            }
        }
    }

}
