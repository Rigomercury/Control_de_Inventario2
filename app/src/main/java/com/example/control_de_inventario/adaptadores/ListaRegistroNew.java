package com.example.control_de_inventario.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.control_de_inventario.R;
import com.example.control_de_inventario.entidades.Articulos2;

import java.util.ArrayList;
import java.util.List;

public class ListaRegistroNew extends RecyclerView.Adapter<ListaRegistroNew.RegistroViewHolder> {

    List<Articulos2> rvNewRegistro;

    public ListaRegistroNew(List<Articulos2> rvNewRegistro){
        this.rvNewRegistro = rvNewRegistro;
    }
    @NonNull
    @Override
    public RegistroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista2_codigos,null,false);
        return new RegistroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaRegistroNew.RegistroViewHolder holder, int position) {
        holder.tvCode.setText("Code: " + rvNewRegistro.get(position).getCode());
        holder.tvCodigo.setText("Codigo: " + rvNewRegistro.get(position).getCodigo());
        holder.tvDescripcion.setText("Descripcion: " + rvNewRegistro.get(position).getDescripcion());
        holder.tvTalla.setText("Talla: " + rvNewRegistro.get(position).getTalla());

    }

    @Override
    public int getItemCount() {return rvNewRegistro.size();}

    public class RegistroViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode,tvCodigo,tvDescripcion,tvTalla;
        public RegistroViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCode = itemView.findViewById(R.id.tvCode);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvTalla = itemView.findViewById(R.id.tvTalla);
        }
    }
}
