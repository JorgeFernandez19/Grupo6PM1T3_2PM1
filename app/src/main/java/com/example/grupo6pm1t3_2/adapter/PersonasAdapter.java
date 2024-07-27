package com.example.grupo6pm1t3_2.adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo6pm1t3_2.R;
import com.example.grupo6pm1t3_2.modelo.Personas;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PersonasAdapter extends FirebaseRecyclerAdapter<Personas, PersonasAdapter.PersonasViewHolder> {

    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Personas persona);
    }

    public PersonasAdapter(@NonNull FirebaseRecyclerOptions<Personas> options, OnItemClickListener listener) {
        super(options);
        this.onItemClickListener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull PersonasViewHolder holder, int position, @NonNull Personas model) {
        holder.textNombres.setText(model.getNombres());
        holder.textApellidos.setText(model.getApellidos());
        holder.textCorreo.setText(model.getCorreo());
        holder.textFechanac.setText(model.getFechanac());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(model));
    }

    @NonNull
    @Override
    public PersonasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_persona, parent, false);
        return new PersonasViewHolder(view);
    }

    public static class PersonasViewHolder extends RecyclerView.ViewHolder {
        TextView textNombres;
        TextView textApellidos;
        TextView textCorreo;
        TextView textFechanac;

        public PersonasViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombres = itemView.findViewById(R.id.textNombres);
            textApellidos = itemView.findViewById(R.id.textApellidos);
            textCorreo = itemView.findViewById(R.id.textCorreo);
            textFechanac = itemView.findViewById(R.id.textFechanac);
        }
    }
}
