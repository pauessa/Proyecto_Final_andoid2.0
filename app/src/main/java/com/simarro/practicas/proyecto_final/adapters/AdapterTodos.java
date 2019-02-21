package com.simarro.practicas.proyecto_final.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simarro.practicas.proyecto_final.R;
import com.simarro.practicas.proyecto_final.SqlLite;
import com.simarro.practicas.proyecto_final.pojo.Libro;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterTodos extends RecyclerView.Adapter<AdapterTodos.Libroviewholder>{

    List<Libro> libros;
    private final OnItemClickListener listener;
    int pos;

    public AdapterTodos(List<Libro> libros, OnItemClickListener listener) {
        this.libros = libros;
        this.listener=listener;
    }



    @Override
    public Libroviewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
       View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_libros,viewGroup,false);
       Libroviewholder holder=new Libroviewholder(v);
       return holder;

    }

    @Override
    public void onBindViewHolder(Libroviewholder libroviewholder, int i) {
        final Libro l=libros.get(i);
        l.setPos(i);
        libroviewholder.titulo.setText(l.getTitulo());

        if(l.getPortada()==null){
            Picasso.get().load(libroviewholder.db.getimage()).into( libroviewholder.portada);
        }else{
            Picasso.get().load(l.getPortada()).into( libroviewholder.portada);

        }
        libroviewholder.autor.setText(l.getAutor().getNombre());
        libroviewholder.npag.setText(Integer.toString(l.getnPaginas()));
        String s="";
        for (int j = 0; j < l.getValoracionMedia(); j++) {
           s+="★";
        }

        libroviewholder.valoracion.setText(s);
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
        TextView titulo,autor,npag,valoracion;
        ImageView portada;
        ConstraintLayout lineaLeidos;
        SqlLite db;
        public Libroviewholder( View itemView) {
            super(itemView);
            titulo=itemView.findViewById(R.id.titulo);
            portada=itemView.findViewById(R.id.imgPortada);
            autor=itemView.findViewById(R.id.autor);
            npag=itemView.findViewById(R.id.npag);
            valoracion=itemView.findViewById(R.id.valoracion);
            lineaLeidos=itemView.findViewById(R.id.lineaLeidos);
            db =new SqlLite(itemView.getContext(), "Imagensql", null, 1);


        }
    }
    public interface OnItemClickListener {
        void onItemClick(Libro item);
    }
    public int getItemSelected(){
        return pos;
    }
}
