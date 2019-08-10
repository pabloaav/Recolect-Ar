package com.e.recolectar.logica.vista;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.e.recolectar.R;
import com.e.recolectar.adaptadores.AdaptadorPaginas;
import com.e.recolectar.fragmentos.EstadoFragment;
import com.e.recolectar.fragmentos.IncidenciaFragment;
import com.e.recolectar.fragmentos.PuntoReciclajeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;


public class MenuInicio extends AppCompatActivity implements EstadoFragment.OnFragmentInteractionListener, IncidenciaFragment.OnFragmentInteractionListener, PuntoReciclajeFragment.OnFragmentInteractionListener {


    //region ATRIBUTOS
    private int[] tabIcons = {
            R.drawable.incidencia,
            R.drawable.ic_estado,
            R.drawable.puntos};
    private FirebaseAuth mAuth;
    private static final String TAG = "Error: " ;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);
        try {
            AdaptadorPaginas sectionsPagerAdapter = new AdaptadorPaginas(this, getSupportFragmentManager());
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);

            // Para poner iconos
            tabs.getTabAt(0).setIcon(tabIcons[0]);
            tabs.getTabAt(1).setIcon(tabIcons[1]);
            tabs.getTabAt(2).setIcon(tabIcons[2]);

            //Si hay Menu, existe un usuario, luego:
            mAuth = FirebaseAuth.getInstance();

            //Mostramos el usuario en el SnackBAr
            Snackbar.make(viewPager, "Bienvenido: " + mAuth.getCurrentUser().getEmail(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//Fin de metodo onCreate()

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void logOut(View v) {
        if (v.getId() == R.id.btn_cerrar_sesion) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Cerrar Sesión ?");
            dialogo1.setMessage("Estás seguro de que quieres cerrar la sesión ?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    aceptarCerrarSesion();
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.dismiss();
                }
            });
            dialogo1.show();
        }
    }

    public void deleteUser(View v) {
        if (v.getId() == R.id.btn_delete_user) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Eliminar datos de usuario");
            dialogo1.setMessage("Vas a eliminar tus datos de usuario. Se perderán las incidencias realizadas. ¿Deseas continuar?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    aceptaDeleteUser();
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.dismiss();
                }
            });
            dialogo1.show();
        }

    }

    private void aceptaDeleteUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            AlertDialog.Builder dialogo2 = new AlertDialog.Builder(MenuInicio.this);
                            dialogo2.setTitle("Esta es una operación sensible");
                            dialogo2.setMessage("Debes cerrar sesión y volver a autenticarte. ¿Deseas continuar?");
                            dialogo2.setCancelable(false);
                            dialogo2.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    aceptarCerrarSesion();
                                }
                            });
                            dialogo2.setNegativeButton("Descartar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    dialogo1.dismiss();
                                }
                            });
                            dialogo2.show();
                            Log.e(TAG, "Delete user failure", task.getException());
                        } else {
                            //Do anything here which needs to be done after delete user is complete
                            Intent delete = new Intent(MenuInicio.this, MainActivity.class);
                            delete.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(delete);
                            finish();
                        }

                    }
                });
    }

    private void aceptarCerrarSesion() {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    //Do anything here which needs to be done after signout is complete
                    Intent login = new Intent(MenuInicio.this, MainActivity.class);
                    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(login);
                    finish();
                } else {
                }
            }
        };

        //Init and attach
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(authStateListener);

        //Call signOut()
        mAuth.signOut();
    }

}