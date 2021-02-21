package net.iessochoa.tomassolerlinares.practica7.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import net.iessochoa.tomassolerlinares.practica7.MainActivity;
import net.iessochoa.tomassolerlinares.practica7.R;
import net.iessochoa.tomassolerlinares.practica7.adapters.ChatAdapter;
import net.iessochoa.tomassolerlinares.practica7.model.Conferencia;
import net.iessochoa.tomassolerlinares.practica7.model.FirebaseContract;
import net.iessochoa.tomassolerlinares.practica7.model.Mensaje;

import java.util.ArrayList;

public class InicioAppActivity extends AppCompatActivity {
    private static final String TAG = "InicioAppActivity.class";
    public static String usuario;
    //Instancia de autenticación
    FirebaseAuth auth;

    ArrayList<Conferencia> listaConferencias;
    Spinner spListaConferencias;
    TextView tvDatosConferenciaIniciada, tvDatosNombre, tvDatosCorreo;
    ImageButton ibInfoConferencia, ibEnviar;
    RecyclerView rvChat;
    EditText edtMensaje;
    ChatAdapter adapter;

    private Conferencia conferenciaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_app);
        auth = FirebaseAuth.getInstance();
        //Se cargan los datos en sus contenedores del xml
        tvDatosNombre = findViewById(R.id.tvDatosNombre);
        tvDatosCorreo = findViewById(R.id.tvDatosCorreo);
        tvDatosConferenciaIniciada = findViewById(R.id.tvDatosConferenciaIniciada);
        spListaConferencias = findViewById(R.id.spListaConferencias);
        ibInfoConferencia = findViewById(R.id.ibInfoConferencia);
        ibEnviar = findViewById(R.id.ibEnviar);
        edtMensaje = findViewById(R.id.edtMensajes);
        rvChat = findViewById(R.id.rvMensajes);

        //RecyclerView
        rvChat = findViewById(R.id.rvMensajes);
        rvChat.setLayoutManager(new LinearLayoutManager(this));

        //Se cargan las conferencias almacenadas en firebase
        leerConferencias();
        iniciarConferenciasIniciadas();

        //mostramos los datos del usuario en el campo de texto
        FirebaseUser usrFB = auth.getCurrentUser();
        tvDatosNombre.setText(usrFB.getDisplayName());
        usuario = usrFB.getDisplayName();
        tvDatosCorreo.setText(usrFB.getEmail());

        //Muestra la información de la conferencia seleccionada
        ibInfoConferencia.setOnClickListener(v -> {
            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(InicioAppActivity.this);
            builder.setTitle(R.string.infoConferencia);

            int i = spListaConferencias.getSelectedItemPosition();
            Conferencia conferencia = listaConferencias.get(i);
            String mensaje = conferencia.getNombre() + getString(R.string.saltoLinea) + getString(R.string.fecha) + conferencia.getFechaFormatoLocal()
                    + getString(R.string.saltoLinea) + getString(R.string.horario) + conferencia.getHorario() + getString(R.string.saltoLinea) + getString(R.string.sala) + conferencia.getSala();
            builder.setMessage(mensaje);

            builder.setPositiveButton(getString(R.string.OK), (dialog, id) -> dialog.cancel());

            builder.create().show();
        });

        //Almacena en conferenciaActual la conferencia y define el adaptador del chat dependiendo de la conferencia
        spListaConferencias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String conferencia = (String) spListaConferencias.getSelectedItem();
                for (int i = 0; i < listaConferencias.size(); i++) {
                    if (conferencia.equals(listaConferencias.get(i).getNombre())) {
                        conferenciaActual = listaConferencias.get(i);
                        defineAdaptador();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Envía un mensaje al pulsar el botón enviar.
        ibEnviar.setOnClickListener(v -> {
            enviarMensaje();
        });
    }

    //es necesario parar la escucha
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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

    //Método que lee las conferencias almacenadas en firebase y las carga en el spinner
    private void leerConferencias() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listaConferencias = new ArrayList<>();
        db.collection(FirebaseContract.ConferenciaEntry.NODE_NAME).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    listaConferencias.add(document.toObject(Conferencia.class));
                }
                cargaSpinner();
            } else {
                Log.d(TAG, "Error getting documents: ",
                        task.getException());
            }
        });
    }

    //Carga los nombres de las conferencias almacenadas en el spinner
    private void cargaSpinner() {

        String[] arraySpinner = new String[listaConferencias.size()];
        for (int i = 0; i < arraySpinner.length; i++) {
            arraySpinner[i] = listaConferencias.get(i).getNombre();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        spListaConferencias.setAdapter(adapter);
        String conferencia = (String) spListaConferencias.getSelectedItem();
        for (int i = 0; i < listaConferencias.size(); i++) {
            if (conferencia.equals(listaConferencias.get(i).getNombre())) {
                conferenciaActual = listaConferencias.get(i);
                defineAdaptador();
            }
        }
    }

    //Método que devuelve la conferencia iniciada actualmente
    private void iniciarConferenciasIniciadas() {
        //https://firebase.google.com/docs/firestore/query-data/listen
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef =
                db.collection(FirebaseContract.ConferenciaIniciadaEntry.NODE_NAME).document(FirebaseContract.ConferenciaIniciadaEntry.ID);
        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                String conferenciaIniciada = snapshot.getString(FirebaseContract.ConferenciaIniciadaEntry.CONFERENCIA);
                tvDatosConferenciaIniciada.setText(conferenciaIniciada);
                Log.d(TAG, "Conferencia iniciada: " + snapshot.getData());
            } else {
                Log.d(TAG, "Current data: null");
            }
        });
    }

    private void enviarMensaje() {
        String body = edtMensaje.getText().toString();
        if (!body.isEmpty()) {
            //usuario y mensaje
            Mensaje mensaje = new Mensaje(usuario, body);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(FirebaseContract.ConferenciaEntry.NODE_NAME)
                    //documento conferencia actual
                    .document(conferenciaActual.getId())
                    //subcolección de la conferencia
                    .collection(FirebaseContract.ChatEntry.NODE_NAME)
                    //añadimos el mensaje nuevo
                    .add(mensaje);
            edtMensaje.setText("");
            ocultarTeclado();
        }
    }

    /**
     * Permite ocultar el teclado
     */
    private void ocultarTeclado() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(edtMensaje.getWindowToken(), 0);
        }
    }

    private void defineAdaptador() {
        //consulta en Firebase
        Query query = FirebaseFirestore.getInstance()
                //coleccion conferencias
                .collection(FirebaseContract.ConferenciaEntry.NODE_NAME)
                //documento: conferencia actual
                .document(conferenciaActual.getId())
                //colección chat de la conferencia
                .collection(FirebaseContract.ChatEntry.NODE_NAME)
                //obtenemos la lista ordenada por fecha
                .orderBy(FirebaseContract.ChatEntry.FECHA_CREACION,
                        Query.Direction.DESCENDING);
        //Creamos la opciones del FirebaseAdapter
        FirestoreRecyclerOptions<Mensaje> options = new FirestoreRecyclerOptions.Builder<Mensaje>()
                //consulta y clase en la que se guarda los datos
                .setQuery(query, Mensaje.class)
                .setLifecycleOwner(this)
                .build();
        //si el usuario ya habia seleccionado otra conferencia, paramos las escucha
        if (adapter != null) {
            adapter.stopListening();
        }
        //Creamos el adaptador
        adapter = new ChatAdapter(options);
        //asignamos el adaptador
        rvChat.setAdapter(adapter);
        //comenzamos a escuchar. Normalmente solo tenemos un adaptador, esto tenemos que
        //hacerlo en el evento onStar, como indica la documentación
        adapter.startListening();
        //Podemos reaccionar ante cambios en la query( se añade un mesaje).  Nosotros,
        // //lo que necesitamos es mover el scroll
        // del recyclerView al inicio para ver el mensaje nuevo
        adapter.getSnapshots().addChangeEventListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull
                    DocumentSnapshot snapshot, int newIndex, int oldIndex) {
                rvChat.smoothScrollToPosition(0);
            }

            @Override
            public void onDataChanged() {
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
            }
        });
    }


}