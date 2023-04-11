package com.example.control_de_inventario.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.control_de_inventario.R;
import com.example.control_de_inventario.entidades.Articulos;

import java.util.ArrayList;

public class ListaRegistrosAdapter extends RecyclerView.Adapter<ListaRegistrosAdapter.RegistroViewHolder> {

    ArrayList<Articulos> listaRegistro;

    public ListaRegistrosAdapter(ArrayList<Articulos> listaRegistro){
        this.listaRegistro = listaRegistro;
    }

    @NonNull
    @Override
    public RegistroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_codigos,null,false);
        return new RegistroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaRegistrosAdapter.RegistroViewHolder holder, int position) {
        holder.viewCode.setText(listaRegistro.get(position).getCode());
        holder.viewCodigo.setText(listaRegistro.get(position).getCodigo());
        holder.viewDescripcion.setText(listaRegistro.get(position).getDescripcion());
        holder.viewTalla.setText(listaRegistro.get(position).getTalla());
    }

    @Override
    public int getItemCount() {
        return listaRegistro.size();
    }

    public class RegistroViewHolder extends RecyclerView.ViewHolder {
        TextView viewCode, viewCodigo, viewDescripcion, viewTalla;
        public RegistroViewHolder(@NonNull View itemView) {
            super(itemView);

            viewCode = itemView.findViewById(R.id.viewCode);
            viewCodigo = itemView.findViewById(R.id.viewCodigo);
            viewDescripcion = itemView.findViewById(R.id.viewDescripcion);
            viewTalla = itemView.findViewById(R.id.viewTalla);
        }
    }
}
