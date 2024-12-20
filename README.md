# Jespada-app
Repositorio de la aplicación Jespada Video.



# Configuración de los permisos de la aplicacion.
Para que la aplicacion pueda utilizar la camara y el microfono estos permisos deben de declararse en el manifes.    
   
**Declaración de permisos en fichero AndroidManifest.xml**   
- La primera línea asegura el acceso a Internet.   
- La segunda línea permite a la app escribir en almacenamiento externo.    
- La tercera línea define que la app requiere acceso obligatorio al hardware de la cámara.    
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-feature android:name="android.hardware.camera" android:required="true"/>
```

**Solicitud de permiso al usuario en tiempo de ejecución:**

```kotlin
// Declaramos una constante para identificar la solicitud de permisos
private static final int REQUEST_CODE = 100; // Código único para manejar la solicitud de permisos

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main); // Establecemos el diseño de la actividad

    // Configuramos un botón que permitirá al usuario solicitar los permisos
    findViewById(R.id.requestPermissionButton).setOnClickListener(v -> checkAndRequestPermissions());
    // Cuando se hace clic en el botón con el ID "requestPermissionButton", se llama al método "checkAndRequestPermissions"
}

/**
 * Método para comprobar si los permisos ya están concedidos y solicitarlos si no lo están.
 */
private void checkAndRequestPermissions() {
    // Verificamos si el permiso de escritura en almacenamiento externo o el permiso de cámara NO están concedidos
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

        // Si al menos uno de los permisos no está concedido, solicitamos ambos permisos al usuario
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                REQUEST_CODE);
    } else {
        // Si los permisos ya están concedidos, mostramos un mensaje de confirmación
        Toast.makeText(this, "Permisos ya concedidos", Toast.LENGTH_SHORT).show();
    }
}

/**
 * Método que maneja la respuesta del usuario a la solicitud de permisos.
 * Este método es llamado automáticamente después de que el usuario decide otorgar o denegar los permisos.
 */
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    // Verificamos si el código de solicitud coincide con nuestra constante REQUEST_CODE
    if (requestCode == REQUEST_CODE) {
        // Comprobamos si la respuesta contiene al menos un resultado y si el primer permiso fue concedido
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Si el permiso fue concedido, mostramos un mensaje de éxito
            Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
        } else {
            // Si el permiso fue denegado, mostramos un mensaje informando al usuario
            Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
        }
    }
}
```
