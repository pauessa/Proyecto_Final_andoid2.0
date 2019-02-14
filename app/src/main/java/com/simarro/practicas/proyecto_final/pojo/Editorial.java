package com.simarro.practicas.proyecto_final.pojo;

import java.util.Date;

public class Editorial {
    private String nombre;
    private Date fechaDeCreacion;


    public Editorial() {
    }

    public Editorial(String nombre, Date fechaDeCreacion) {
        this.nombre = nombre;
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(Date fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    @Override
    public String toString() {
        return "Editorial{" +
                "nombre='" + nombre + '\'' +
                ", fechaDeCreacion=" + fechaDeCreacion +
                '}';
    }
}
