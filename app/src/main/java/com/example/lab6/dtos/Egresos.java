package com.example.lab6.dtos;

public class Egresos {

    private String titulo;
    private double monto;
    private String descripcion;
    private String date;

    public Egresos() {
        // Constructor vacío necesario para Firebase
    }
    public Egresos(String titulo, double monto, String descripcion, String date) {
        this.titulo = titulo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.date = date;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
