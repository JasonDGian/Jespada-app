package com.dasus.jesapadavideoapp

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.dasus.jesapadavideoapp.GrabarActivity.Companion.CODIGO_PERMISOS
import com.dasus.jesapadavideoapp.GrabarActivity.Companion.PERMISOS_REQUERIDOS
import android.view.animation.AnimationUtils


// Autor : David Jason Gianmoena.
class MainActivity : AppCompatActivity() {

    // Propiedad que indica si los permisos fueron concedidos
    private var permisosConcedidos: Boolean = false

    // Componentes visuales para las diferentes partes del logo.
    private lateinit var imagen_logo_3: ImageView
    private lateinit var imagen_logo_2: ImageView
    private lateinit var imagen_logo_21: ImageView
    private lateinit var imagen_logo_22: ImageView
    private lateinit var imagen_logo_1: ImageView
    private lateinit var logoMenu: ImageView

    // Animaciones asociadas al logo y los elementos visuales.
    private lateinit var animEspera: Animation
    private lateinit var animEspera2: Animation
    private lateinit var animJespada: Animation
    private lateinit var animEspada: Animation
    private lateinit var animEspadaLenta: Animation
    private lateinit var animEspadaLenta2: Animation
    private lateinit var animArriba: Animation
    private lateinit var animArriba2: Animation
    private lateinit var espadaAparecer: Animation

    // Reproductor multimedia para los efectos de sonido.
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ------------------- BLOQUE DE CONFIGURACIÓN DE LA TOOLBAR --------------------------
        // En este bloque se invoca y configura la toolbar para esta pantalla, utilizando el layout
        // comun a todas las actividades.

        // Configurar la Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_main)
        // Configura la toolbar personalizada para la pantalla.
        setSupportActionBar(toolbar)
        // Desactiva el titulo del app (en favor del logo.)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // Recupera el icono para la toolbar.
        val iconoToolbar = AppCompatResources.getDrawable(this, R.drawable.menu_icon)
        // Configura el icono del boton de la toolbar.
        toolbar.overflowIcon = iconoToolbar
        // ------------------------------------------------------------------------------

        // -------------------------- BLOQUE INICIO EJECUCION --------------------------

        // Inicializa los elementos esteticos y las animaciones + reproductor de sonido.
        inicializaElementos();

        // Inicia la animación de carga inicial para los elementos del logo.
        animacionEspera()

        // ------------------------------------------------------------------------------


        // -------------------- BLOQUE DE CONFIGURACIÓN DE ANIMACIONES Y NAVEGACIÓN --------------------
        // En este bloque se define el flujo de las animaciones que gestionan la presentación de la
        // aplicación. Se establece un orden específico mediante los 'listeners', que también se
        // encargan de controlar la navegación.Este mecanismo garantiza que la animación se ejecute
        // completamente antes de invocar la vista o actividad correspondiente para cambiar de
        // pantalla. Esta estrategia se adopta exclusivamente para mejorar la presentación visual
        // de la aplicación.

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

    // ---------------------------------------------------------------------------------------------

    // ------------------------------ BLOQUE DEFINICION FUNCIONES  ---------------------------------
    // En este bloque definimos las funciones utilizadas para la, inicializacion de la aplicacion,
    // la gestión de los elementos del menu de la toolbar y las funciones encargadas ded permisos.

    /**
     * Funcion que inicializa los varios elementos esteticos involucrados en la pantalla principal.
     * Elementos inicializados : Imagenes, animaciones y reproductor de sonido.
     */
    private fun inicializaElementos() {
        // Inicializa los componentes visuales.
        imagen_logo_3 = findViewById(R.id.logo_dinamico_3)
        imagen_logo_2 = findViewById(R.id.logo_dinamico_2)
        imagen_logo_21 = findViewById(R.id.logo_dinamico_2_1)
        imagen_logo_22 = findViewById(R.id.logo_dinamico_2_2)
        imagen_logo_1 = findViewById(R.id.logo_dinamico_1)

        logoMenu = findViewById<ImageView>(R.id.toolbar_logo)
        logoMenu.isVisible = false

        // Carga todas las animaciones
        animEspera = AnimationUtils.loadAnimation(this, R.anim.anim_espera)
        animEspera2 = AnimationUtils.loadAnimation(this, R.anim.anim_espera2)
        animJespada = AnimationUtils.loadAnimation(this, R.anim.anim_jespada)
        animEspada = AnimationUtils.loadAnimation(this, R.anim.anim_espada)
        animEspadaLenta = AnimationUtils.loadAnimation(this, R.anim.anim_espada_lenta)
        animEspadaLenta2 = AnimationUtils.loadAnimation(this, R.anim.anim_espada_lenta2)
        animArriba = AnimationUtils.loadAnimation(this, R.anim.enviar_arriba)
        animArriba2 = AnimationUtils.loadAnimation(this, R.anim.enviar_arriba)
        espadaAparecer = AnimationUtils.loadAnimation(this, R.anim.logo_espada_aparecer)

        // Configura el reproductor de audio para los efectos de sonido de la espada.
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido_espada2)
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

// ----------------------------- FUNCIONES MENU TOOLBAR -------------------------------------------

    /**
     * Funcion que "infla" el boton del menu con las opciones pasadas por parametro.
     * En este caso infla con las funciones especificas de la pantalla principal para acceder
     * a las pantallas de grabar o reproducir.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Infla el menú; agrega elementos al Toolbar
        menuInflater.inflate(R.menu.opciones_menu, menu)
        return true
    }

    /**
     * Funcion que segun el elemento seleccionado en el menú llevará a una pantalla u otra.
     * Gestiona las opciones que aparecen al pinchar en el icono de la toolbar.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            // Opcion de menu para acceder a la pantalla de 'Grabar'.
            R.id.opcion_grabar -> {
                verificarYPedirPermisos()

                // Verifica si los permisos necesarios han sido concedidos
                if (!permisosConcedidos)
                {
                    // Muestra un mensaje al usuario indicando que se necesitan los permisos
                    Toast.makeText(this, "Para el correcto funcionamiento de la aplicación deberá conceder los permisos necesarios.", Toast.LENGTH_SHORT).show()
                    return true
                }

                // Inicia la animación para mover todos los elementos del logo hacia arriba.
                // En el listener de esta animación se encuentra el cambio de actividad.
                // Esto está hecho asi para que se pueda observar la animación completa antes de
                // realizar el cambio de pantallas.
                imagen_logo_1.startAnimation(animArriba)
                imagen_logo_2.startAnimation(animArriba)
                imagen_logo_3.startAnimation(animArriba)
                imagen_logo_21.startAnimation(animArriba)
                imagen_logo_22.startAnimation(animArriba)

                // True para indicar que la opción ha sido "manejada"
                return true
            }

            // Opcion de menu para acceder a la pantalla de 'Reproducir'.
            R.id.opcion_reproducir -> {
                // Lanza la pantalla de grabar.
                imagen_logo_1.startAnimation(animArriba2)
                imagen_logo_2.startAnimation(animArriba2)
                imagen_logo_3.startAnimation(animArriba2)
                imagen_logo_21.startAnimation(animArriba2)
                imagen_logo_22.startAnimation(animArriba2)
                true
            }
            // Opcion default que gestiona cualquier otro input que no sea una opción de menu.
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    // ----------------------------- FUNCIONES PERMISOS -------------------------------------------

    /**
     * Funcion que maneja los resultados de la respuesta del usuario al ser prompteado
     * para conceder o negar permisos a la aplicación.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODIGO_PERMISOS)
        {
            permisosConcedidos = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Funcion que promptea al usuario para conceder los permisos necesarios para el correcto funcionamiento
     * de la aplicación.
     */
    private fun verificarYPedirPermisos() {
        // Comprueba si los permisos ya están concedidos.
        val permisosNoConcedidos = PERMISOS_REQUERIDOS.filter { permiso ->
            ActivityCompat.checkSelfPermission(this, permiso) != PackageManager.PERMISSION_GRANTED
        }

        // Si hay permisos no concedidos, solicita al usuario que los conceda / niegue.
        if (permisosNoConcedidos.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permisosNoConcedidos.toTypedArray(), CODIGO_PERMISOS)
        }
        else
        {
            // Si todos los permisos están concedidos, actualiza el valor booleano o 'flag'.
            permisosConcedidos = true
        }
    }


}
