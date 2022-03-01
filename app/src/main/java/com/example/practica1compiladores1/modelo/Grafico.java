package com.example.practica1compiladores1.modelo;

public class Grafico {
    private TipoGrafico tipoGrafico;
    private String titulo;
    private Unir unir;

    public Grafico(TipoGrafico tipoGrafico, String tituloGrafico, Unir posicionesUnir){
        this.tipoGrafico = tipoGrafico;
        titulo = tituloGrafico;
        unir = posicionesUnir;
    }

    public TipoGrafico getTipoGrafico() {
        return tipoGrafico;
    }

    public String getTitulo() {
        return titulo;
    }

    public Unir getUnir() {
        return unir;
    }

}
