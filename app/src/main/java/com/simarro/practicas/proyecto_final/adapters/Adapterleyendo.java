package com.simarro.practicas.proyecto_final.adapters;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simarro.practicas.proyecto_final.R;
import com.simarro.practicas.proyecto_final.pojo.Libro;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapterleyendo extends RecyclerView.Adapter<Adapterleyendo.Libroviewholder> {

    List<Libro> libros;
    ProgressBarAnimation anim;
    private final OnItemClickListener listener;

    public Adapterleyendo(List<Libro> libros, OnItemClickListener listener) {
        this.libros = libros;
        this.listener = listener;

    }

    @Override
    public Libroviewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_leyendo, viewGroup, false);
        Libroviewholder holder = new Libroviewholder(v);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(Libroviewholder libroviewholder, int i) {
        final Libro l = libros.get(i);
        libroviewholder.titulo.setText(l.getTitulo());
        Picasso.get().load(l.getPortada()).into(libroviewholder.portada);
        if (l.getHojasleidas()==0){
            anim = new ProgressBarAnimation(libroviewholder.progressBar, 0, 0);
        }else{
            anim = new ProgressBarAnimation(libroviewholder.progressBar, 0, (l.getHojasleidas()*100)/l.getnPaginas());
        }
        anim.setDuration(1000);
        libroviewholder.progressBar.startAnimation(anim);
        libroviewholder.porcentaje.setText(l.getHojasleidas()+"/"+l.getnPaginas());
        if (l.getHojasleidas()==0){
            libroviewholder.porcentaje2.setText("0%");
        }else{
            libroviewholder.porcentaje2.setText((l.getHojasleidas()*100)/l.getnPaginas()+"%");
        }
        libroviewholder.progressBar.setScaleY(3f);


        libroviewholder.lineaLeidos.setOnClickListener(new View.OnClickListener() {

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

    public static class Libroviewholder extends RecyclerView.ViewHolder {
        TextView titulo,porcentaje,porcentaje2;
        ImageView portada;
        ConstraintLayout lineaLeidos;
        ProgressBar progressBar;

        public Libroviewholder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo);
            portada = itemView.findViewById(R.id.imgPortada);
            lineaLeidos = itemView.findViewById(R.id.lineaLeidos);
            progressBar = itemView.findViewById(R.id.progressBar);
            porcentaje=itemView.findViewById(R.id.porcentaje);
            porcentaje2=itemView.findViewById(R.id.porcentaje2);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Libro item);
    }

    public class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float  to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }
}
