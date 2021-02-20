package net.iessochoa.tomassolerlinares.practica7.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.iessochoa.tomassolerlinares.practica7.R;
import net.iessochoa.tomassolerlinares.practica7.model.Empresa;
import net.iessochoa.tomassolerlinares.practica7.model.FirebaseContract;

public class EmpresaActivity extends AppCompatActivity {

    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);
        obtenDatosEmpresa();
    }
    //No Funciona
    //Método para leer los datos solamente una vez de Firebase
    void obtenDatosEmpresa() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(FirebaseContract.EmpresaEntry.NODE_NAME).document(FirebaseContract.EmpresaEntry.ID);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            //leemos los datos
            empresa = documentSnapshot.toObject(Empresa.class);
            //mostramos los datos y asignamos eventos
            TextView tvNombreEmpresa = findViewById(R.id.tvNombreEmpresa);
            TextView tvDireccionTexto = findViewById(R.id.tvDireccionTexto);
            TextView tvTelefonoTexto = findViewById(R.id.tvTelefonoTexto);
            tvNombreEmpresa.setText(empresa.getNombre());
            tvDireccionTexto.setText(empresa.getDireccion());
            tvTelefonoTexto.setText(empresa.getTelefono());
            tvDireccionTexto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Llamada a la actividad implicita de la localizacion
                    Intent intent = new Intent(Intent.ACTION_VIEW, empresa.getUriLocalizacion());
                    startActivity(intent);
                }
            });

            tvTelefonoTexto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Llamada a la actividad implicita del telefono
                    startActivity(new Intent(Intent.ACTION_VIEW, empresa.getUriTelefono()));
                }
            });
        });
    }
}