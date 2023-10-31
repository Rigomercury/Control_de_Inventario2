package com.example.control_de_inventario.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.control_de_inventario.R;
import com.example.control_de_inventario.VerActivity;
import com.example.control_de_inventario.entidades.ArticuloInv;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class RecyclerViewAdapterInv extends RecyclerView.Adapter<RecyclerViewAdapterInv.ViewHolder> {

    Context context;
    List<ArticuloInv> articuloInvList;
    List<ArticuloInv> articuloInvListOriginal;

    public RecyclerViewAdapterInv(Context context, List<ArticuloInv> articuloInvList) {
        this.articuloInvList = articuloInvList;
        this.context = context;
        articuloInvListOriginal = new ArrayList<>();
        articuloInvListOriginal.addAll(articuloInvList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inv, parent, false);
        return new RecyclerViewAdapterInv.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvCodigo.setText("Codigo: " + articuloInvList.get(position).getCodigo());
        holder.tvDescripcion.setText("Descripcion: " + articuloInvList.get(position).getDescripcion());
        holder.tvTalla.setText("Talla: " + articuloInvList.get(position).getTalla());
        holder.tvStock.setText("Stock: " + articuloInvList.get(position).getStock());
    }

    public void filtrado(String svConsulta) {
        int longitud = svConsulta.length();
        if (longitud == 0) {
            ;
            articuloInvList.clear();
            articuloInvList.addAll(articuloInvListOriginal);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<ArticuloInv> coleccion = articuloInvList.stream()
                        .filter(i -> i.getCodigo().toLowerCase().contains(svConsulta.toLowerCase()))
                        .collect(Collectors.toList());
                articuloInvList.clear();
                articuloInvList.addAll(coleccion);
            } else {
                for (ArticuloInv c : articuloInvListOriginal) {
                    if (c.getCodigo().toLowerCase().contains(svConsulta.toLowerCase())) {
                        articuloInvList.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return articuloInvList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCodigo, tvDescripcion, tvTalla, tvStock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvTalla = itemView.findViewById(R.id.tvTalla);
            tvStock = itemView.findViewById(R.id.tvStock);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, VerActivity.class);
                    int id = articuloInvList.get(getAdapterPosition()).getId();
                    intent.putExtra("ID", id);
                    context.startActivity(intent);
                }
            });
        }
    }
}
