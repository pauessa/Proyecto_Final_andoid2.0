package com.simarro.practicas.proyecto_final.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simarro.practicas.proyecto_final.R;
import com.simarro.practicas.proyecto_final.pojo.Libro;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterLeidos extends RecyclerView.Adapter<AdapterLeidos.Libroviewholder>{

    List<Libro> libros;
    private final OnItemClickListener listener;

    public AdapterLeidos(List<Libro> libros,OnItemClickListener listener) {
        this.libros = libros;
        this.listener=listener;

    }

    @Override
    public Libroviewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
       View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_deseados,viewGroup,false);
       Libroviewholder holder=new Libroviewholder(v);
       return holder;
    }

    @Override
    public void onBindViewHolder(Libroviewholder libroviewholder, int i) {
        final Libro l=libros.get(i);
        libroviewholder.titulo.setText(l.getTitulo());
        Picasso.get().load(l.getPortada()).into( libroviewholder.portada);

        libroviewholder.lineaLeidos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                listener.onItemClick(l);
            }
        });
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public static class Libroviewholder extends RecyclerView.ViewHolder{
        TextView titulo;
        ImageView portada;
        ConstraintLayout lineaLeidos;
        public Libroviewholder( View itemView) {
            super(itemView);
            titulo=itemView.findViewById(R.id.titulo);
            portada=itemView.findViewById(R.id.imgPortada);
            lineaLeidos=itemView.findViewById(R.id.lineaLeidos);

        }
    }
    public interface OnItemClickListener {
        void onItemClick(Libro item);
    }
}
