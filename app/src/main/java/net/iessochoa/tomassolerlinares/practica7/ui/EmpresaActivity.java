package net.iessochoa.tomassolerlinares.practica7.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import net.iessochoa.tomassolerlinares.practica7.R;
import net.iessochoa.tomassolerlinares.practica7.model.Empresa;
import net.iessochoa.tomassolerlinares.practica7.model.FirebaseContract;

/**
 * Clase Activity encargada de cargar los datos de la empresa de firebase y mostrarlos.
 */
public class EmpresaActivity extends AppCompatActivity {

    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);
        //Se obtienen los datos de la empresa
        obtenDatosEmpresa();
    }

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

            //OPTATIVA 7.2 - 1
            ImageView ivLogo = findViewById(R.id.ivLogo);
            StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();
            StorageReference ref = mImageStorage.child("imagenes")
                    .child("SeveroLogo.png");

            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downUri = task.getResult();
                        String imageUrl = downUri.toString();
                        Glide.with(getApplicationContext()).load(imageUrl).into(ivLogo);
                    }else{
                        Toast.makeText(EmpresaActivity.this, "ERROR "+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });



            tvNombreEmpresa.setText(empresa.getNombre());
            tvDireccionTexto.setText(empresa.getDireccion());
            tvTelefonoTexto.setText(empresa.getTelefono());
            tvDireccionTexto.setOnClickListener(v -> {
                // Llamada a la actividad implicita de la localizacion
                Intent intent = new Intent(Intent.ACTION_VIEW, empresa.getUriLocalizacion());
                startActivity(intent);
            });

            tvTelefonoTexto.setOnClickListener(v -> {
                // Llamada a la actividad implicita del telefono
                startActivity(new Intent(Intent.ACTION_VIEW, empresa.getUriTelefono()));
            });
        });
    }
}