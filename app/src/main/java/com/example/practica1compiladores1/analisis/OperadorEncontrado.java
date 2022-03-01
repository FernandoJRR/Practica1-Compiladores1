package com.example.practica1compiladores1.analisis;

public class OperadorEncontrado {
    private String operador;
    private int[] posicion;
    private String ocurrencia;

    public OperadorEncontrado(String operador, int[] posicion, String ocurrencia){
        this.operador = operador;
        this.posicion = posicion;
        this.ocurrencia = ocurrencia;
    }

    public String getOperador() {
        return operador;
    }

    public int[] getPosicion() {
        return posicion;
    }

    public String getOcurrencia() {
        return ocurrencia;
    }
}
