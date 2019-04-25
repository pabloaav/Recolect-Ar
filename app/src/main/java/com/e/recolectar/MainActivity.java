package com.e.recolectar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/*pongo estas lineas para probar un update*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //Metodo Boton Siguiente
    public void CrearNuevaCuenta(View view){
        Intent crearNuevaCuenta = new Intent(this, CrearCuenta.class);
        startActivity(crearNuevaCuenta);
    }

}
