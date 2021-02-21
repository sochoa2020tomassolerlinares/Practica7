package net.iessochoa.tomassolerlinares.practica7.adapters;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.iessochoa.tomassolerlinares.practica7.R;
import net.iessochoa.tomassolerlinares.practica7.model.Mensaje;
import net.iessochoa.tomassolerlinares.practica7.ui.InicioAppActivity;

public class ChatAdapter extends FirestoreRecyclerAdapter<Mensaje, ChatAdapter.ChatHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatAdapter(@NonNull FirestoreRecyclerOptions<Mensaje> options) {
        super(options);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onBindViewHolder(@NonNull ChatAdapter.ChatHolder holder, int position, @NonNull Mensaje mensaje) {
        if (mensaje != null) {
            //si el mensaje es del usuario lo colocamos a la izquierda
            if (InicioAppActivity.usuario != null && mensaje.getUsuario() != null) {
                String usuario = InicioAppActivity.usuario;
                if (mensaje.getUsuario().equals(usuario)) {
                    holder.cvMensaje.setCardBackgroundColor(Color.CYAN);
                    holder.lytMensaje.setGravity(Gravity.LEFT);
                } else {
                    holder.cvMensaje.setCardBackgroundColor(Color.LTGRAY);
                    holder.lytMensaje.setGravity(Gravity.RIGHT);
                }
            }

            holder.tvMensaje.setText(mensaje.getUsuario() + " => " + mensaje.getBody());
        }
    }

    @NonNull
    @Override
    public ChatAdapter.ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //fijaros que le pasamos el layout que representa cada elemento
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mensaje_item, parent, false);
        return new ChatHolder(itemView);
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        private final TextView tvMensaje;
        private final CardView cvMensaje;
        private final LinearLayout lytMensaje;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            lytMensaje = itemView.findViewById(R.id.lytMensajes);
            cvMensaje = itemView.findViewById(R.id.cvMensaje);
            tvMensaje = itemView.findViewById(R.id.tvMensajes);
        }
    }
}
