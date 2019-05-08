package com.e.recolectar;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //region Declaracion de Variables
    private EditText correoLogin, contrasena;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextInputLayout til_correoLogin, til_contrasena;
    private FirebaseAuth firebaseAuth; //Objeto de Firebase para autenticar
    private GoogleSignInClient mGoogleSignInClient;

    //endregion

    //region Metodos del SetUp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Vinculacion Logica-Grafica

        correoLogin = (EditText) findViewById(R.id.txt_correoLogin);
        contrasena = (EditText) findViewById(R.id.txt_contrasena);
        til_correoLogin = (TextInputLayout) findViewById(R.id.til_correoLogin);
        til_contrasena = (TextInputLayout) findViewById(R.id.til_contrasena);


//        inicializacion de Firebase
        inicializarFirebase();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        barra de progreso de accion

        Toast.makeText(this, "Bienvenido a Recolect-Ar", Toast.LENGTH_LONG).show();

    }// Fin de metodo onCreate()

    //Inicializacion de objetos de Firebase
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

//        inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    /*Actualizar la UI si ya esta logueado*/
    private void updateUI(FirebaseUser user) {
    }
    //endregion

    //region Ingreso Correo y Password

    //Metodo de autenticar
    public void autenticar() {

//        Obtenemos el string de cada campo
        String p_correoLogin = correoLogin.getText().toString().trim();
        String p_contrasena = contrasena.getText().toString().trim();
        boolean correOk;
        boolean passOk;

//        Validamos el correo
        if (p_correoLogin.isEmpty()) {
            til_correoLogin.setError("Ingresa tu correo por favor");
            correOk = false;
        } else if (!Validar.validarEmail(p_correoLogin)) {
            til_correoLogin.setError("Ingresa un correo valido: nombre@ejemplo.com");
            correOk = false;
        } else {
            til_correoLogin.setError(null);
            correOk = true;
        }
//        Validamos la contrasena
        if (p_contrasena.isEmpty()) {
            til_contrasena.setError("Ingresa tu contraseña por favor");
            passOk = false;
        } else if (!Validar.validarPassword(p_contrasena)) {
            til_contrasena.setError("Minimo 4 caracteres, incluido un numero");
            passOk = false;
        } else {
            til_contrasena.setError(null);
            passOk = true;
        }

//        Si todo esta OK procedemos...
        if (correOk && passOk) {
            loguearEmailPassword(p_correoLogin, p_contrasena);


        }

    }

    //Metodo de Login con correo y contraseña
    private void loguearEmailPassword(String p_correoLogin, String p_contrasena) {

        firebaseAuth.signInWithEmailAndPassword(p_correoLogin, p_contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                             String correo = firebaseAuth.getInstance().getCurrentUser().getEmail();
                            Toast.makeText(MainActivity.this, "Bienvenido: " + correo, Toast.LENGTH_LONG).show();
//                            Intent inicio = new Intent(getApplication(), InicioActivity.class);
//                            startActivity(inicio);
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(MainActivity.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });
    }
    //endregion

    //region Ingreso Google

    //Metodo para Login con Google
    public void signInConGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Bienvenido: " + user.getEmail(), Toast.LENGTH_LONG).show();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "No se pudo iniciar sesion ", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "No se pudo inciar", Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }
    //endregion

    //region Otros Metodos

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_loguear:
                autenticar();
                break;

            case R.id.bt_loggoogle:
                signInConGoogle();
                break;

            case R.id.bt_registrarse:
                crearNuevaCuenta();
                break;

            case R.id.bt_logface:
                //login Facebook
                break;

            default:
                Toast.makeText(this, "No se pudo iniciar sesion", Toast.LENGTH_LONG).show();
                break;
        }
    }

    //Metodo del boton Crear Nueva Cuenta
    public void crearNuevaCuenta() {
        Intent crearCuenta = new Intent(this, CrearCuenta.class);
        startActivity(crearCuenta);

    }
    //endregion

}
