package com.e.recolectar.logica.vista;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.e.recolectar.R;
import com.e.recolectar.fragmentos.EstadoFragment;
import com.e.recolectar.fragmentos.IncidenciaFragment;
import com.e.recolectar.fragmentos.PuntoReciclajeFragment;
import com.e.recolectar.logica.CrearCuenta;
import com.e.recolectar.logica.LoginMVP;
import com.e.recolectar.logica.presentacion.LoginPresentador;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements LoginMVP.Vista, EstadoFragment.OnFragmentInteractionListener, IncidenciaFragment.OnFragmentInteractionListener, PuntoReciclajeFragment.OnFragmentInteractionListener {

    //region Declaracion de Variables
    private EditText correoLogin, contrasena;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextInputLayout til_correoLogin, til_contrasena;
    private FirebaseAuth firebaseAuth; //Objeto de Firebase para autenticar
    private GoogleSignInClient mGoogleSignInClient;
    private String p_correoLogin;
    private String p_contrasena;
    private LoginMVP.Presentacion loginPresentador;


    //endregion

    //region Metodos del SetUp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Vinculacion Logica-Grafica
        bindLayoutElements();

//        inicializacion de Firebase
        inicializarFirebase();


        //Inicializacion del Presentador del Login
        loginPresentador = new LoginPresentador(this, this, firebaseAuth, databaseReference);

        //Configuracion de inicio con Cuenta Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }// Fin de metodo onCreate()

    private void bindLayoutElements() {
        correoLogin = findViewById(R.id.txt_correoLogin);
        contrasena = findViewById(R.id.txt_contrasena);
        til_correoLogin = findViewById(R.id.til_correoLogin);
        til_contrasena = findViewById(R.id.til_contrasena);
//        correoLogin.setText("pabloperez@gmail.com");
//        contrasena.setText("prueba4");

    }

    //---------------------------------------------------------------------------------------------

    //Inicializacion de objetos de Firebase
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

//        inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//        Toast.makeText(MainActivity.this, "Verificando sus Datos", Toast.LENGTH_SHORT).show();
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                String p_correoLogin = correoLogin.getText().toString().trim();
//                String p_contrasena = contrasena.getText().toString().trim();
//                try {
//                    loginPresentador.doLoginWhitEmailPassword(p_correoLogin, p_contrasena);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, 2000);
//    }

//    @SuppressLint("WrongConstant")
//    @Override
//    public void onStart() {
//        super.onStart();
//        Toast.makeText(MainActivity.this, "Verificando sus Datos", Toast.LENGTH_SHORT).show();
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Check if user is signed in (non-null) and update UI accordingly.
//                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//                if (currentUser != null) {
//                    Intent menu = new Intent(MainActivity.this, MenuInicio.class);
//                    menu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivity(menu);
//                    finish();
//                } else {
////                    String p_correoLogin = correoLogin.getText().toString().trim();
////                    String p_contrasena = contrasena.getText().toString().trim();
////                    try {
////                        loginPresentador.doLoginWhitEmailPassword(p_correoLogin, p_contrasena);
////
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
//                }
//            }
//
//    },2000);
//
////        updateUI(currentUser);
//}

    /*Se realizan acciones de exito al ingresar con cuenta de Google*/
    private void updateUI(FirebaseUser user) {
        Intent menu = new Intent(MainActivity.this, MenuInicio.class);
        menu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(menu);
        onSuccess();
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

    //  private void bindInputData() {
//        String p_correoLogin = "pabloperez@gmail.com";
//        String p_contrasena = "prueba4";
//        String p_correoLogin = correoLogin.getText().toString().trim();
//        String p_contrasena = contrasena.getText().toString().trim();
//    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_loguear:
                bindLayoutElements();
                String p_correoLogin = correoLogin.getText().toString().trim();
                String p_contrasena = contrasena.getText().toString().trim();

                try {
                    loginPresentador.doLoginWhitEmailPassword(p_correoLogin, p_contrasena);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.bt_loggoogle:
                signInConGoogle();
                break;

            case R.id.bt_registrarse:
                crearNuevaCuenta();
                break;



            default:
                Toast.makeText(this, "No se pudo iniciar sesion", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void crearNuevaCuenta() {
        Intent crearCuenta = new Intent(this, CrearCuenta.class);
        startActivity(crearCuenta);

    }

    @Override
    public void onEmailValidationError(String errorType) {
        this.til_correoLogin.setError(errorType);
    }

    @Override
    public void onPassValidationError(String errorType) {
        this.til_contrasena.setError(errorType);
    }

    @Override
    public void onError(String error) {
        View vista = findViewById(R.id.linearMainActivity);
        Snackbar.make(vista, error, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public void onSuccess() {
        this.finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


//    private void abrirGaleria() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/");
//        startActivityForResult(Intent.createChooser(intent, "Seleccione la aplicacion"), 10);
//    }


    //endregion

}//Fin de la clase MainActivity
