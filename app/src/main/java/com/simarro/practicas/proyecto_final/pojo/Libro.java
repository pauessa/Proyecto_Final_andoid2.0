package com.simarro.practicas.proyecto_final.pojo;

import java.io.Serializable;
import java.util.Date;

public class Libro implements Serializable {

    private String titulo;
    private String portada;
    private Autor autor;
    private int nPaginas;
    private String isbn;
    private Editorial editorial;
    private Date fechaLanzamiento;
    private String sinopsis;
    private String nombreSaga;
    private String lengua;
    private String genero;
    private int valoracionMedia;
    private int pos;

    public Libro() {

    }
    public Libro(String titulo) {
        this.titulo = titulo;
    }

    public Libro(String titulo, Autor autor, int nPaginas, String isbn, Editorial editorial, Date fechaLanzamiento, String sinopsis, String nombreSaga, String lengua, String genero, int valoracionMedia) {
        this.titulo = titulo;
        this.autor = autor;
        this.nPaginas = nPaginas;
        this.isbn = isbn;
        this.editorial = editorial;
        this.fechaLanzamiento = fechaLanzamiento;
        this.sinopsis = sinopsis;
        this.nombreSaga = nombreSaga;
        this.lengua = lengua;
        this.genero = genero;
        this.valoracionMedia = valoracionMedia;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public int getnPaginas() {
        return nPaginas;
    }

    public void setnPaginas(int nPaginas) {
        this.nPaginas = nPaginas;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public Date getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(Date fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getNombreSaga() {
        return nombreSaga;
    }

    public void setNombreSaga(String nombreSaga) {
        this.nombreSaga = nombreSaga;
    }

    public String getLengua() {
        return lengua;
    }

    public void setLengua(String lengua) {
        this.lengua = lengua;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getValoracionMedia() {
        return valoracionMedia;
    }

    public void setValoracionMedia(int valoracionMedia) {
        this.valoracionMedia = valoracionMedia;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "titulo='" + titulo + '\'' +
                ", autor=" + autor +
                ", nPaginas=" + nPaginas +
                ", isbn='" + isbn + '\'' +
                ", editorial=" + editorial +
                ", fechaLanzamiento=" + fechaLanzamiento +
                ", sinopsis='" + sinopsis + '\'' +
                ", nombreSaga='" + nombreSaga + '\'' +
                ", lengua='" + lengua + '\'' +
                ", genero='" + genero + '\'' +
                ", valoracionMedia=" + valoracionMedia +
                '}';
    }
}
