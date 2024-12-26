package com.dasus.jesapadavideoapp

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.content.res.AppCompatResources
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.PermissionChecker
import com.dasus.jesapadavideoapp.databinding.ActivityGrabarBinding
import java.io.File

// Clase que maneja la actividad para grabar video.
class GrabarActivity : AppCompatActivity()
{

    companion object
    {
        private const val TAG = "Jespada"
        private const val NOMBRE_ARCHIVO = "Video"
        const val CODIGO_PERMISOS = 10
        val PERMISOS_REQUERIDOS = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
            {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

    // Declaración de variables necesarias para la grabación y la cámara.
    private lateinit var viewBinding: ActivityGrabarBinding
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private lateinit var cameraExecutor: ExecutorService

    // Método llamado al crear la actividad.
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityGrabarBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Configura la Toolbar y oculta el título
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_grabar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.overflowIcon = AppCompatResources.getDrawable(this, R.drawable.menu_icon)

        // Configura el estado inicial de los botones
        indicarBotonDetenerDisabled()

        // Verifica si los permisos están concedidos antes de iniciar la cámara
        if (todosLosPermisosConcedidos())
        {
            iniciarCamara()
        }


        // Acción para iniciar grabación al hacer clic en el botón de grabar
        viewBinding.cardGrabar.setOnClickListener {
            comprobarSiVideoExisteYEliminarlo()
            Toast.makeText(baseContext, "Grabando...", Toast.LENGTH_SHORT).show()
            capturarVideo()
            indicarBotonGrabarDisabled()
        }

        // Acción para detener la grabación
        viewBinding.cardDetener.setOnClickListener {
            capturarVideo()
            indicarBotonDetenerDisabled()
        }

        // Inicia un ejecutor para manejar tareas de cámara
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    // Desactiva visualmente el botón de detener
    private fun indicarBotonDetenerDisabled()
    {
        viewBinding.cardDetener.setCardBackgroundColor(resources.getColor(R.color.azuldisabled))
        viewBinding.ivDetener.setImageResource(R.mipmap.boton_detener_disabled)
        viewBinding.cardGrabar.setCardBackgroundColor(resources.getColor(R.color.azulclaromenu))
        viewBinding.ivGrabar.setImageResource(R.mipmap.boton_grabar)
    }

    // Desactiva visualmente el botón de grabar
    private fun indicarBotonGrabarDisabled()
    {
        viewBinding.cardGrabar.setCardBackgroundColor(resources.getColor(R.color.azuldisabled))
        viewBinding.ivGrabar.setImageResource(R.mipmap.boton_grabar_disabled)
        viewBinding.cardDetener.setCardBackgroundColor(resources.getColor(R.color.azulclaromenu))
        viewBinding.ivDetener.setImageResource(R.mipmap.boton_detener)
    }

    // Verifica si existe un video grabado y lo elimina si es necesario
    private fun comprobarSiVideoExisteYEliminarlo()
    {
        val videoDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), TAG)
        if (videoDirectory.exists())
        {
            val videoFile = File(videoDirectory, "Video.mp4")
            if (videoFile.exists() && !videoFile.delete())
            {
                Log.e("comprobarSiVideoExiste", "No se pudo eliminar el archivo Video.mp4.")
            }
        }
    }

    // Captura o detiene la grabación del video
    private fun capturarVideo()
    {
        val videoCapture = this.videoCapture ?: return
        viewBinding.cardGrabar.isEnabled = false
        viewBinding.cardDetener.isEnabled = false

        // Si ya hay una grabación en curso, se detiene
        val curRecording = recording
        if (curRecording != null)
        {
            curRecording.stop()
            recording = null
            return
        }

        // Configura las opciones de salida del video en el almacenamiento
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, NOMBRE_ARCHIVO)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
            {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Jespada")
            }
        }

        val opcionesSalidaMediaStore = MediaStoreOutputOptions.Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        // Prepara y comienza la grabación
        recording = videoCapture.output
            .prepareRecording(this, opcionesSalidaMediaStore)
            .apply {
                if (PermissionChecker.checkSelfPermission(this@GrabarActivity, Manifest.permission.RECORD_AUDIO) == PermissionChecker.PERMISSION_GRANTED)
                {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(this))
            { eventoGrabacion ->
                when (eventoGrabacion) {
                    is VideoRecordEvent.Start -> viewBinding.cardDetener.isEnabled = true
                    is VideoRecordEvent.Finalize ->
                        {
                        if (!eventoGrabacion.hasError())
                        {
                            Toast.makeText(baseContext, "Video grabado con éxito", Toast.LENGTH_SHORT).show()
                        } else
                        {
                            Log.e(TAG, "La grabación terminó con error: ${eventoGrabacion.error}")
                        }
                        viewBinding.cardGrabar.isEnabled = true
                        viewBinding.cardDetener.isEnabled = false
                    }
                }
            }
    }

    // Inicia la cámara
    private fun iniciarCamara()
    {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewBinding.pvCamara.surfaceProvider)
            }

            val recorder = Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.HIGHEST)).build()
            videoCapture = VideoCapture.withOutput(recorder)
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try
            {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture)
            } catch (exc: Exception)
            {
                Log.e(TAG, "Falló la vinculación del caso de uso", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    // Verifica si todos los permisos requeridos han sido concedidos
    private fun todosLosPermisosConcedidos() = PERMISOS_REQUERIDOS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    // Destruye la ejecucion de la camara
    override fun onDestroy()
    {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    // Configura el menú de opciones
    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.opciones_menu_grabar, menu)
        return true
    }

    // Maneja la selección de opciones del menú
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.opcion_volver -> {
            startActivity(Intent(this, MainActivity::class.java))
            true
        }
        R.id.opcion_reproducir -> {
            startActivity(Intent(this, ReproducirActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
