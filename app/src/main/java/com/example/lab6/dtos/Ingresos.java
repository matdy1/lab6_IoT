package com.example.lab6.dtos;

import java.io.Serializable;
import java.util.Date;

public class Ingresos implements Serializable {
    private String titulo;
    private double monto;
    private String descripcion;
    private Date dueDate;

    public Ingresos() {
        // Constructor vacío necesario para Firebase
    }

    public Ingresos(String titulo, double monto, String descripcion, Date dueDate) {
        this.titulo = titulo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.dueDate = dueDate;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
