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
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.PermissionChecker
import androidx.core.content.res.ResourcesCompat
import com.dasus.jesapadavideoapp.databinding.ActivityGrabarBinding
import java.io.File

class GrabarActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityGrabarBinding
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityGrabarBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_grabar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setOverflowIcon(ResourcesCompat.getDrawable(resources, R.drawable.menu_icon, null))

        indicarBotonDetenerDisabled()

        // Pedir permisos de cámara
        if (todosLosPermisosConcedidos()) {
            iniciarCamara()
        } else {
            ActivityCompat.requestPermissions(
                this, PERMISOS_REQUERIDOS, CODIGO_PERMISOS)
        }

        viewBinding.cardGrabar.setOnClickListener {
            comprobarSiVideoExisteYEliminarlo()
            Toast.makeText(baseContext, "Grabando...", Toast.LENGTH_SHORT).show()
            capturarVideo()
            indicarBotonGrabarDisabled()
        }

        viewBinding.cardDetener.setOnClickListener {
            capturarVideo()
            indicarBotonDetenerDisabled()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun indicarBotonDetenerDisabled() {
        viewBinding.cardDetener.setCardBackgroundColor(resources.getColor(R.color.azuldisabled))
        viewBinding.ivDetener.setImageResource(R.mipmap.boton_detener_disabled)
        viewBinding.cardGrabar.setCardBackgroundColor(resources.getColor(R.color.azulclaromenu))
        viewBinding.ivGrabar.setImageResource(R.mipmap.boton_grabar)
    }

    private fun indicarBotonGrabarDisabled() {
        viewBinding.cardGrabar.setCardBackgroundColor(resources.getColor(R.color.azuldisabled))
        viewBinding.ivGrabar.setImageResource(R.mipmap.boton_grabar_disabled)
        viewBinding.cardDetener.setCardBackgroundColor(resources.getColor(R.color.azulclaromenu))
        viewBinding.ivDetener.setImageResource(R.mipmap.boton_detener)
    }

    private fun comprobarSiVideoExisteYEliminarlo() {
        // Obtener el directorio "Movies"
        val videoDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), TAG)

        // Verificar si el directorio existe
        if (videoDirectory.exists()) {
            // Crear el archivo de video con el nombre "Video.mp4"
            val videoFile = File(videoDirectory, "Video.mp4")

            // Verificar si el archivo existe
            if (videoFile.exists()) {
                // Eliminar el archivo si existe
                val eliminado = videoFile.delete()
                if (eliminado) {
                    // Si el archivo fue eliminado correctamente
                    Log.d("comprobarSiVideoExiste", "El archivo Video.mp4 fue eliminado correctamente.")
                } else {
                    // Si no se pudo eliminar el archivo
                    Log.e("comprobarSiVideoExiste", "No se pudo eliminar el archivo Video.mp4.")
                }
            } else {
                // Si el archivo no existe
                Log.d("comprobarSiVideoExiste", "El archivo Video.mp4 no existe.")
            }
        } else {
            // Si el directorio no existe
            Log.d("comprobarSiVideoExiste", "El directorio $TAG no existe")
        }
    }

    // Implementa el caso de uso VideoCapture, incluyendo inicio y paro de la grabación.
    private fun capturarVideo() {
        val videoCapture = this.videoCapture ?: return

        viewBinding.cardGrabar.isEnabled = false
        viewBinding.cardDetener.isEnabled = false

        val curRecording = recording
        if (curRecording != null) {
            // Detener la grabación actual.
            curRecording.stop()
            recording = null
            return
        }

        // Crear y comenzar una nueva sesión de grabación
        val nombre = NOMBRE_ARCHIVO
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, nombre)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Jespada")
            }
        }

        val opcionesSalidaMediaStore = MediaStoreOutputOptions
            .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        recording = videoCapture.output
            .prepareRecording(this, opcionesSalidaMediaStore)
            .apply {
                if (PermissionChecker.checkSelfPermission(this@GrabarActivity,
                        Manifest.permission.RECORD_AUDIO) ==
                    PermissionChecker.PERMISSION_GRANTED)
                {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(this)) { eventoGrabacion ->
                when(eventoGrabacion) {
                    is VideoRecordEvent.Start -> {
                        viewBinding.apply {
                            cardDetener.isEnabled = true

                        }
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (!eventoGrabacion.hasError()) {
                            val mensaje = "Video grabado con exito"
                            Toast.makeText(baseContext, mensaje, Toast.LENGTH_SHORT)
                                .show()
                            Log.d(TAG, mensaje)
                        } else {
                            recording?.close()
                            recording = null
                            Log.e(TAG, "La grabación terminó con error: " +
                                    "${eventoGrabacion.error}")
                        }
                        viewBinding.apply {
                            cardGrabar.isEnabled = true
                            cardDetener.isEnabled = false
                        }
                    }
                }
            }
    }

    private fun iniciarCamara() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Usar para vincular el ciclo de vida de las cámaras al ciclo de vida del propietario
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Vista previa
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.pvCamara.surfaceProvider)
                }

            // Capturador de video
            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            // Seleccionar la cámara trasera como predeterminada
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Desvincular los casos de uso antes de volver a vincular
                cameraProvider.unbindAll()

                // Vincular los casos de uso a la cámara
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Falló la vinculación del caso de uso", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun todosLosPermisosConcedidos() = PERMISOS_REQUERIDOS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "Jespada"
        private const val NOMBRE_ARCHIVO = "Video"
        private const val CODIGO_PERMISOS = 10
        private val PERMISOS_REQUERIDOS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    // Método de IA modificado
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults) // Llamada a la implementación de la superclase

        if (requestCode == CODIGO_PERMISOS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
            } else {
                // Permiso denegado
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Infla el menú; agrega elementos al Toolbar
        menuInflater.inflate(R.menu.opciones_menu_grabar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.opcion_volver -> {
            startActivity(Intent(this, MainActivity::class.java))
            true
        }
        R.id.opcion_reproducir -> {
            startActivity(Intent(this, GrabarActivity::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
