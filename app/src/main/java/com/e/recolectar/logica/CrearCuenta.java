package com.e.recolectar.logica;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.e.recolectar.R;
import com.e.recolectar.modelo.Usuario;
import com.e.recolectar.validaciones.Validar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CrearCuenta extends AppCompatActivity {

    //region Declaracion de Variables
    private FirebaseAuth firebaseAuth; //Objeto de Firebase para autenticar
    private EditText dni, nombre, apellido, email, password, reppass;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextInputLayout til_dni, til_nombre, til_apellido, til_email, til_password, til_reppass;
    private ProgressDialog progressDialog;//Objeto que muestra una barra de proceso
    //endregion

    //region setUp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

//        Vinculacion Logica-Grafica
//        EditText
        dni = findViewById(R.id.txt_dni);
        nombre = findViewById(R.id.txt_nombre);
        apellido = findViewById(R.id.txt_apellido);
        email = findViewById(R.id.txt_mail);
        password = findViewById(R.id.txt_pass);
        reppass = findViewById(R.id.txt_reppass);
//        TextInputLayout
        til_dni = findViewById(R.id.til_dni);
        til_nombre = findViewById(R.id.til_nombre);
        til_apellido = findViewById(R.id.til_apellido);
        til_email = findViewById(R.id.til_mail);
        til_password = findViewById(R.id.til_pass);
        til_reppass = findViewById(R.id.til_reppass);
//        inicializacion de Firebase
        inicializarFirebase();
//        barra de progreso
        progressDialog = new ProgressDialog(this);
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
//        inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
    }
    //endregion

    //region Registacion
    public void registrar(View view) {
        //        Obtenemos el string de cada campo
        String p_dni = dni.getText().toString().trim();
        String p_nombre = nombre.getText().toString().trim();
        String p_apellido = apellido.getText().toString().trim();
        String p_email = email.getText().toString().trim();
        String p_password = password.getText().toString().trim();
        String p_reppass = reppass.getText().toString().trim();


//        Validar los campos del Registro de Usuario
        boolean passwordOk = Validar.validarPassword(p_password);
        boolean emailOk = Validar.validarEmail(p_email);

//        si no pasan las validaciones, reportamos al usuario
        if (p_dni.isEmpty()) {
            til_dni.setError("Por Favor, ingrese su numero de documento de identidad");
        } else if (p_nombre.isEmpty()) {
            til_nombre.setError("Por Favor, ingrese su nombre");
        } else if (p_apellido.isEmpty()) {
            til_apellido.setError("Por Favor, ingrese su/s apellidos");
        } else if (!emailOk) {
            til_email.setError("Siga el Formato correcto");
        } else if (!passwordOk) {
            til_password.setError("Minimo 6 caracteres, incluido un numero");

        } else if (p_password.equals(p_reppass)) {
//            Setear en null los posibles errores de input
            til_dni.setError(null);
            til_nombre.setError(null);
            til_apellido.setError(null);
            til_email.setError(null);
            til_password.setError(null);
//        Hechas las validaciones, se procede al registro
            progressDialog.setMessage("Realizando registro en linea...");
            progressDialog.show();
//        Crear un Usuario para guardar
//            Primero creamos usuario con email y contraseña para la autentiacion de Firebase
            String p_id = crearNuevoUsuario(p_email, p_password);
//            Con el id que genera la autenticacion en Firebase, seteamos nuestro usuario
            Usuario usuario = new Usuario(p_id, p_dni, p_nombre, p_apellido, p_email, p_password);
//            El Usuario con su id y demas datos se guarda en la base de datos
            guardar(usuario);

        } else {
            Toast.makeText(this, "Por Favor, verifique el password", Toast.LENGTH_LONG).show();
        }

    }

    private void guardar(Usuario p_usuario) {

//        Con la referencia a Base de Datos (BD), creamos un hijo (child) Usuarios, un child Id, y los datos
        databaseReference.child("Usuarios").child(p_usuario.getIdUsuario()).setValue(p_usuario);
    }

    private String crearNuevoUsuario(String p_email, String p_password) {

        firebaseAuth.createUserWithEmailAndPassword(p_email, p_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(CrearCuenta.this, "Se ha registrado el usuario con el email: " + email.getText(), Toast.LENGTH_LONG).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(CrearCuenta.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }

                        progressDialog.dismiss();

                    }

                });

        return firebaseAuth.getCurrentUser().getUid();
    }
    //endregion

    //region Otros Metodos
    /*Actualizar la UI si ya esta logueado*/
    private void updateUI(FirebaseUser user) {
    }
    //endregion

}
