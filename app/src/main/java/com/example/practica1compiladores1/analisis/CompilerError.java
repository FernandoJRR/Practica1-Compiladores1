package com.example.practica1compiladores1.analisis;

public class CompilerError {
    private String simbolo;
    private int[] posicion;
    private TipoError tipo;
    private String descripcion;

    public CompilerError(String simbolo, int[] posicion, TipoError tipo, String descripcion){
        this.simbolo = simbolo;
        this.posicion = posicion;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public int[] getPosicion() {
        return posicion;
    }

    public TipoError getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
