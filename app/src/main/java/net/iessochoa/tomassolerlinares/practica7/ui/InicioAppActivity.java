package net.iessochoa.tomassolerlinares.practica7.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.iessochoa.tomassolerlinares.practica7.MainActivity;
import net.iessochoa.tomassolerlinares.practica7.R;

public class InicioAppActivity extends AppCompatActivity {
    //Instancia de autenticación
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_app);
        auth = FirebaseAuth.getInstance();
        TextView tvDatosNombre = findViewById(R.id.tvDatosNombre);
        TextView tvDatosCorreo = findViewById(R.id.tvDatosCorreo);
        //mostramos los datos del usuario en el campo de texto
        FirebaseUser usrFB= auth.getCurrentUser();
        tvDatosNombre.setText(usrFB.getDisplayName());
        tvDatosCorreo.setText(usrFB.getEmail());
    }

    //Inicia el menú
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio_app, menu);
        return true;
    }

    //Establece las opciones de los items del menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //permite cerrar la sesión y cerrar la actividad para volver a la actividad principal
            case R.id.action_cerrarSesion:
                auth.signOut();
                startActivity(new Intent(InicioAppActivity.this, MainActivity.class));
                finish();
                return true;
            //lanza la activity con la información de la empresa
            case R.id.action_infoEmpresa:
                startActivity(new Intent(InicioAppActivity.this, EmpresaActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}