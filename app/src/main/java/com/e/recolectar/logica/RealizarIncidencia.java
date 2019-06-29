package com.e.recolectar.logica;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.TextInputLayout;
import com.e.recolectar.R;
import com.e.recolectar.logica.modelo.Incidencia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RealizarIncidencia extends AppCompatActivity {

    //region ATRIBUTOS

    private final String CARPETA_RAIZ = "misImagenesPrueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misFotos";

    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;

    Button botonCargar;
    ImageView imagen;
    String path;
    private Uri miPath;
    private StorageReference mStorageRef;
    private Incidencia incidencia;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    TextView textView_ubicacion, textView_tipo_incidencia;
    EditText editText_descripcion, editText_direccion;
    Location mUbicacion;
    Location location;
    String nombre_tipo_incidencia;
    private static final int CODIGO_SELECCIONAR_UBICACION = 1;
    //endregion

    //region METODOS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_incidencia);

        imagen = findViewById(R.id.imagemId);
        botonCargar = findViewById(R.id.btnCargarImg);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        location = null;
        textView_ubicacion = findViewById(R.id.tv_ubicacion);
        textView_tipo_incidencia = findViewById(R.id.tv_tipo_incidencia);
        editText_descripcion = findViewById(R.id.txt_descripcion);
        editText_direccion = findViewById(R.id.txt_direccion_particular);

        if (validaPermisos()) {
            botonCargar.setEnabled(true);
        } else {
            botonCargar.setEnabled(false);
        }
        Toast.makeText(this, "Paso por onCreate()", Toast.LENGTH_SHORT).show();

        //Recibir los datos a traves del Bundle
        Bundle extraTipoResiduo = getIntent().getExtras();

        //Recibimos los Bundle, de location y tipo de incidencia. Si no son vacios se procede a utilizar la informacion
        if (extraTipoResiduo != null) {
            nombre_tipo_incidencia = extraTipoResiduo.getString("tipo");
            if (nombre_tipo_incidencia != null) {
                textView_tipo_incidencia.setText(String.format("El tipo de residuo a recolectar es: %s", nombre_tipo_incidencia));
            }
        }

//        //Verificamos que no sea null
//        if (location != null) {
//            //Si es distinto de null tomamos los datos
//            mUbicacion = location;
//            textView_ubicacion.setText(getStringUbicacion());
//            String cadenaDeUbicacion = getStringUbicacion();
//            textView_ubicacion.setText(cadenaDeUbicacion);
//        }
    }//Fin de onCreate()

    private String getStringUbicacion() {
        String cadenaDeUbicacion = "mi cadena";

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (addresses.size() > 0) {
//                    Address fetchedAddress = addresses.get(0);
//                    StringBuilder strAddress = new StringBuilder();
//                    for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
//                        strAddress.append(fetchedAddress.getAddressLine(i).indexOf(1));
//                        cadenaDeUbicacion = strAddress.toString();}
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                    cadenaDeUbicacion = address;
                } else {
                    textView_ubicacion.setText("Searching Current Address");
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Could not get address..!", Toast.LENGTH_SHORT).show();
            }
        }
        return cadenaDeUbicacion;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        //Verificamos que no sea null la location
//        if (location != null) {
//            textView_ubicacion.setText((int) location.getLatitude());
//        }
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Toast.makeText(this, "Pasa por onStart()", Toast.LENGTH_SHORT).show();
//    }

    private boolean validaPermisos() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if ((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {
            cargarDialogoRecomendacion();
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                botonCargar.setEnabled(true);
            } else {
                solicitarPermisosManual();
            }
        }

    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"si", "no"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RealizarIncidencia.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(RealizarIncidencia.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
            }
        });
        dialogo.show();
    }

    public void onclick(View view) {
        cargarImagen();
    }

    private void cargarImagen() {

        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RealizarIncidencia.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    tomarFotografia();
                } else {
                    if (opciones[i].equals("Cargar Imagen")) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicación"), COD_SELECCIONA);
                    } else {
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();

    }

    private void tomarFotografia() {
        File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();
        String nombreImagen = "";
        if (isCreada == false) {
            isCreada = fileImagen.mkdirs();
        }

        if (isCreada == true) {
            nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";
        }


        path = Environment.getExternalStorageDirectory() +
                File.separator + RUTA_IMAGEN + File.separator + nombreImagen;

        File imagen = new File(path);

        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authorities = getApplicationContext().getPackageName() + ".provider";
            Uri imageUri = FileProvider.getUriForFile(this, authorities, imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent, COD_FOTO);

        ////
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case COD_SELECCIONA:
                    miPath = data.getData();
                    imagen.setImageURI(miPath);
                    break;

                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de almacenamiento", "Path: " + path);
                                }
                            });

                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    imagen.setImageBitmap(bitmap);

                    break;
                case CODIGO_SELECCIONAR_UBICACION:
                    location = data.getParcelableExtra("locacion");

                    //Verificamos que no sea null
                    if (location != null) {
                        //Si es distinto de null tomamos los datos
                        mUbicacion = location;
                        String cadenaDeUbicacion = getStringUbicacion();
                        textView_ubicacion.setText(cadenaDeUbicacion);
                    }

                    break;
            }


        }
    }

    public void subirIncidencia(View view) {

        String tipo = nombre_tipo_incidencia;
        String fecha = this.obtenerFecha();
        Map<String, Object> HM_ubicacion = null;
        String descripcion = editText_descripcion.getText().toString().trim();
        String direccion_particular = editText_direccion.getText().toString().trim();
        ;

        if (mUbicacion != null) {
            //Hacemos el Pojo de la Ubicacion con algunos datos del objeto Location
            HM_ubicacion = crearHashMapUbicacion(mUbicacion);
        }

        //Subir incidencia a la tabla Usuarios //Subir incidencia a ala tabla Situaciones

        incidencia = new Incidencia(tipo, fecha, miPath, descripcion, HM_ubicacion, direccion_particular, this, mDatabase, mAuth, mStorageRef);

        try {
            incidencia.cargarIncidencia();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> crearHashMapUbicacion(Location miUbicacion) {
        double latitud = 0;
        double longitud = 0;
        String cadenaUbicacion = null;

        //Obtenemos los datos de la ubicacion
        if (miUbicacion != null) {
            latitud = miUbicacion.getLatitude();
            longitud = miUbicacion.getLongitude();
            cadenaUbicacion = textView_ubicacion.getText().toString().trim();
        }

        //Ahora el objeto hashMap para subir a la base de datos
        Map<String, Object> ubicacion = new HashMap<>();
        //En el mismo orden de Firebase
        ubicacion.put("latitud", latitud);
        ubicacion.put("longitud", longitud);
        ubicacion.put("cadenaUbicacion", cadenaUbicacion);

        return ubicacion;
    }

    public void subirUbicacion(View view) {
        Intent geoLocalizacion = new Intent(this, MapsActivity.class);
        startActivityForResult(geoLocalizacion, CODIGO_SELECCIONAR_UBICACION);
    }

    private String obtenerFecha() {

        Date date = new Date();
        String stringDate = DateFormat.getDateTimeInstance().format(date);
        return stringDate;
    }

    //endregion
}
