package com.example.control_de_inventario.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.control_de_inventario.R;
import com.example.control_de_inventario.entidades.ArticuloInv;
import com.example.control_de_inventario.entidades.Articulos;

import java.util.List;

public class AdaptadorArticulos extends RecyclerView.Adapter<AdaptadorArticulos.ArticulosViewHolder> {
    Context context;
    List<Articulos> listaArticulos;


    public AdaptadorArticulos(Context context, List<Articulos> listaArticulos){
        this.context = context;
        this.listaArticulos = listaArticulos;
    }

    @NonNull
    @Override
    public AdaptadorArticulos.ArticulosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista2_codigos, null, false);
        return new AdaptadorArticulos.ArticulosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorArticulos.ArticulosViewHolder holder, int position) {
        holder.tvCode.setText(listaArticulos.get(position).getCode());
        holder.tvCodigo.setText(listaArticulos.get(position).getCodigo());
        holder.tvDescripcion.setText(listaArticulos.get(position).getDescripcion());
        holder.tvTalla.setText(listaArticulos.get(position).getTalla());
    }

    @Override
    public int getItemCount() {
        return listaArticulos.size();
    }

    public class ArticulosViewHolder extends RecyclerView.ViewHolder {

        TextView tvCode, tvCodigo, tvDescripcion, tvTalla;

        public ArticulosViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvTalla = itemView.findViewById(R.id.tvTalla);

        }
    }
}
