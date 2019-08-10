package com.e.recolectar.logica;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.e.recolectar.R;
import com.e.recolectar.logica.modelo.Incidencia;
import com.e.recolectar.logica.vista.MenuInicio;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RealizarIncidencia extends AppCompatActivity {

    //region ATRIBUTOS

    //region Atributos final y estaticos. Codigos de Results
    private final String CARPETA_RAIZ = "imagenesRecolectAr/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "RecolectAr";
    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;
    private static final int CODIGO_SELECCIONAR_UBICACION = 1;
    //endregion

    //region Atributos comunes
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
    String nombreImagen = "";
    private ProgressDialog cargandoIncidencia;
    //endregion

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
        cargandoIncidencia = new ProgressDialog(this);

        if (validaPermisos()) {
            botonCargar.setEnabled(true);
        } else {
            botonCargar.setEnabled(false);
        }


        //Recibir los datos a traves del Bundle
        Bundle extraTipoResiduo = getIntent().getExtras();

        //Recibimos los Bundle, de location y tipo de incidencia. Si no son vacios se procede a utilizar la informacion
        if (extraTipoResiduo != null) {
            nombre_tipo_incidencia = extraTipoResiduo.getString("tipo");
            if (nombre_tipo_incidencia != null) {
                textView_tipo_incidencia.setText(String.format("El tipo de residuo a recolectar es: %s", nombre_tipo_incidencia));
            }
        }

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
                    //Quiero obtener solo la calle y el numero; formo un array String
                    String[] arrayCalleNumero = address.split(",");
                    String calleNumero = arrayCalleNumero[0];
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
//                    cadenaDeUbicacion = calleNumero + ", " + city + ", " + state;
                    cadenaDeUbicacion = calleNumero + ", " + city;
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
//        imagen_sin_foto = imagen.getDrawable();
//    }

    private boolean validaPermisos() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if ((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION))
        ) {
            cargarDialogoRecomendacion();
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA, ACCESS_FINE_LOCATION}, 100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
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
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
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

                    String realPath = getRealPathFromURI(miPath);

                    reducirGuardarFotoString(realPath);
                    Glide
                            .with(RealizarIncidencia.this)
                            .load(miPath)
                            .placeholder(R.drawable.icono_sin_foto512x)
                            .error(R.drawable.icono_sin_foto512x)
                            .into(imagen);
                    break;

                case COD_FOTO:

                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de almacenamiento", "Path: " + path);
                                    miPath = uri;
                                }
                            });

//                    Bitmap bitmap = BitmapFactory.decodeFile(path);
//                    imagen.setImageBitmap(bitmap);
                    //Transformo el String path a Uri

                    reducirGuardarFotoString(path);

                    Glide
                            .with(RealizarIncidencia.this)
                            .load(path)
                            .placeholder(R.drawable.icono_sin_foto512x)
                            .error(R.drawable.icono_sin_foto512x)
                            .into(imagen);

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

    public void reducirGuardarFotoString(String path) {

        Bitmap imagenMasReducida = resizeFoto(path);

        String fileName = nombreImagen;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imagenMasReducida.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        File ExternalStorageDirectory = Environment.getExternalStorageDirectory();
        File file = new File(ExternalStorageDirectory + File.separator + RUTA_IMAGEN + File.separator + fileName);
        FileOutputStream fileOutputStream = null;
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes.toByteArray());

            Toast.makeText(RealizarIncidencia.this, "Tu imagen se guardo en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public Bitmap resizeFoto(String foto) {
        Bitmap fotoReducida = null;
        try {
//            byte[] byteCode = Base64.decode(foto, Base64.DEFAULT);
//            //this.imagen= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
//
////            int alto = 100;//alto en pixeles
////            int ancho = 150;//ancho en pixeles

            fotoReducida = BitmapFactory.decodeFile(foto);
//            fotoReducida2 = Bitmap.createScaledBitmap(fotoReducida, alto, ancho, true);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return fotoReducida;
    }

    private boolean onValidationSuccess() {
        boolean validar = false;
        if (miPath == null) {
            mostrarAlertaImagen();
        } else if (textView_ubicacion.getText().toString().equals(getString(R.string.tv_ubicacion)) || textView_ubicacion.getText().toString().isEmpty()) {
            mostrarAlertaUbicacion();
        } else if (editText_descripcion.getText().toString().equals(getString(R.string.txt_descripcion)) || editText_descripcion.getText().toString().isEmpty()) {
            mostrarAlertaDescripcion();
        } else {
            validar = true;
        }

        return validar;
    }

    private void mostrarAlertaDescripcion() {
        final CharSequence[] opciones = {"OK"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RealizarIncidencia.this);
        alertOpciones.setTitle("Por favor, describe la situación");
        alertOpciones.setIcon(R.drawable.ic_error_black_24dp);
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("OK")) {
                    dialogInterface.dismiss();
                }
            }
        });

        alertOpciones.show();
    }

    private void mostrarAlertaUbicacion() {
        final CharSequence[] opciones = {"OK"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RealizarIncidencia.this);
        alertOpciones.setTitle("Por favor, escoge una ubicacion geografica");
        alertOpciones.setIcon(R.drawable.ic_error_black_24dp);
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("OK")) {
                    dialogInterface.dismiss();
                }
            }
        });

        alertOpciones.show();
    }

    private void mostrarAlertaImagen() {
        final CharSequence[] opciones = {"OK"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RealizarIncidencia.this);
        alertOpciones.setTitle("Por favor, escoge una imagen");
        alertOpciones.setIcon(R.drawable.ic_error_black_24dp);
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("OK")) {
                    dialogInterface.dismiss();
                }
            }
        });

        alertOpciones.show();
    }

    public void subirIncidencia(View view) {

        //Mostramos una barra de progreso
        cargandoIncidencia.setMessage("Registrando su incidencia, aguarde por favor...");

        //Antes de cargar la incidencia deben llenarse ciertos campos

        String tipo = nombre_tipo_incidencia;
        String fecha = this.obtenerFecha();
        Map<String, Object> HM_ubicacion = null;
        String descripcion = editText_descripcion.getText().toString().trim();
        String direccion_particular = editText_direccion.getText().toString().trim();

        if (mUbicacion != null) {
            //Hacemos el Pojo de la Ubicacion con algunos datos del objeto Location
            HM_ubicacion = crearHashMapUbicacion(mUbicacion);
        }
        if (direccion_particular.isEmpty()) {
            direccion_particular = "Sin direccion particular";
        }
        //Se forma el objeto incidencia que va a Firebase
        incidencia = new Incidencia(tipo, fecha, miPath, descripcion, HM_ubicacion, direccion_particular);

        boolean pasoLasValidaciones = onValidationSuccess();
        if (pasoLasValidaciones) {
//            Toast.makeText(this, "Falta elegir foto", Toast.LENGTH_SHORT).show();
            try {
                //Subir incidencia a la tabla Usuarios //Subir incidencia a ala tabla Situaciones
                this.cargarIncidencia(incidencia, cargandoIncidencia);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void cargarIncidencia(final Incidencia p_incidencia, final ProgressDialog dialogCargando) {
        dialogCargando.show();
        if (p_incidencia.getImagen() != null) {
            String segmento = p_incidencia.getImagen().getLastPathSegment();
            final StorageReference fotoRef = mStorageRef.child("Fotos").child(mAuth.getCurrentUser().getUid()).child(segmento);

            fotoRef.putFile(p_incidencia.getImagen()).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw new Exception();
                    }

                    return fotoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadLink = task.getResult();
                        Map<String, Object> incidencia = new HashMap<>();
                        //En el mismo orden de Firebase
                        incidencia.put("descripcion", p_incidencia.getDescripcion());
                        incidencia.put("fecha", p_incidencia.getFecha());
                        incidencia.put("imagen", downloadLink.toString());
                        incidencia.put("tipo", p_incidencia.getTipo());
                        incidencia.put("direccion", p_incidencia.getUbicacion());
                        incidencia.put("ubicacion", p_incidencia.getGeo_ubicacion());
//      otra opcion es: mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("situaciones").push().updateChildren(incidencia).addOnCompleteListener(new ...
                        mDatabase.child("Incidencias").child(mAuth.getCurrentUser().getUid()).push().setValue(incidencia);
//                        mDatabase.child("Incidencias").child(mAuth.getCurrentUser().getUid()).push().setValue(location);
                        mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("incidencias").push().setValue(incidencia).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                exitoIncidencia();
                                dialogCargando.dismiss();
//                                Toast.makeText(RealizarIncidencia.this, "Se cargo la incidencia correctamente.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(RealizarIncidencia.this, "Error al cargar la incidencia" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });
        }

    }

    private void exitoIncidencia() {
        final CharSequence[] opciones = {"OK"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RealizarIncidencia.this);
        alertOpciones.setTitle("Su incidencia se cargó correctamente");
        alertOpciones.setIcon(R.drawable.icono_maps_check_white_48x);
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("OK")) {
                    Intent irMenu = new Intent(RealizarIncidencia.this, MenuInicio.class);
//        irMenu.putExtra("Algo");
//                    setResult(RESULT_OK,irMenu);
                    finish();
                } else {
                    Intent irMenu = new Intent(RealizarIncidencia.this, MenuInicio.class);
//        irMenu.putExtra("Algo");
//                    setResult(RESULT_OK,irMenu);
                    finish();
                    dialogInterface.dismiss();
                }
            }
        });

        alertOpciones.show();
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
        Intent geoLocalizacion = new Intent(this, MapsActivity2.class);
        startActivityForResult(geoLocalizacion, CODIGO_SELECCIONAR_UBICACION);
    }

    private String obtenerFecha() {

        Date date = new Date();
        String stringDate = DateFormat.getDateTimeInstance().format(date);
        return stringDate;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }


    //endregion
}
