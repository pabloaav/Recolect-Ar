package com.e.recolectar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //region Declaracion de Variables

    private EditText correoLogin, contrasena;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextInputLayout til_correoLogin, til_contrasena;
    private ProgressDialog progressDialog;//Objeto que muestra una barra de proceso
    private FirebaseAuth firebaseAuth; //Objeto de Firebase para autenticar

    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Vinculacion Logica-Grafica

        correoLogin = findViewById(R.id.txt_correoLogin);
        contrasena = findViewById(R.id.txt_contrasena);
        til_correoLogin = findViewById(R.id.til_correoLogin);
        til_contrasena = findViewById(R.id.til_contrasena);

//        inicializacion de Firebase
        inicializarFirebase();
//        barra de progreso de accion
        progressDialog = new ProgressDialog(this);

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
//        inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void autenticar(View view) {

//        Obtenemos el string de cada campo
        String p_correoLogin = correoLogin.getText().toString().trim();
        String p_contrasena = contrasena.getText().toString().trim();

//        Validamos el correo
        if (p_correoLogin.isEmpty()) {
            til_correoLogin.setError("Ingresa tu correo por favor");
        } else if (!Validar.validarEmail(p_correoLogin)) {
            til_correoLogin.setError("Ingresa un correo valido: nombre@ejemplo.com");
        } else {
            til_correoLogin.setError(null);
        }
//        Validamos la contrasena
        if (p_contrasena.isEmpty()) {
            til_contrasena.setError("Ingresa tu contraseña por favor");
        } else if (!Validar.validarPassword(p_contrasena)) {
            til_contrasena.setError("Minimo 4 caracteres, incluido un numero");
        } else {
            til_contrasena.setError(null);
        }

        progressDialog.setMessage("Realizando registro en linea...");
        progressDialog.show();

        loguearUsuario(p_correoLogin, p_contrasena);

    }

    private void loguearUsuario(String p_correoLogin, String p_contrasena) {

        firebaseAuth.signInWithEmailAndPassword(p_correoLogin, p_contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Bienvenido: " + correoLogin.getText(), Toast.LENGTH_LONG).show();
//                            Intent inicio = new Intent(getApplication(), InicioActivity.class);
//                            startActivity(inicio);
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(MainActivity.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    //Metodo del boton Crear Nueva Cuenta
    public void CrearNuevaCuenta(View view) {
        Intent crearCuenta = new Intent(this, CrearCuenta.class);
        startActivity(crearCuenta);
    }
}
