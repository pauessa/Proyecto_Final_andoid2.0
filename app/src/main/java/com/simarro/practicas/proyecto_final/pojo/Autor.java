package com.simarro.practicas.proyecto_final.pojo;

import java.util.Date;

public class Autor {
    private String nombre;
    private Date fechaNacimiento;
    private int numeroDeLibros;
    private String pais;
    private String biografia;

    public Autor() {
    }

    public Autor(String nombre, Date fechaNacimiento, int numeroDeLibros, String pais, String biografia) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.numeroDeLibros = numeroDeLibros;
        this.pais = pais;
        this.biografia = biografia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getNumeroDeLibros() {
        return numeroDeLibros;
    }

    public void setNumeroDeLibros(int numeroDeLibros) {
        this.numeroDeLibros = numeroDeLibros;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
