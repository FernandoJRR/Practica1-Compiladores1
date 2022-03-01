package com.example.practica1compiladores1.modelo;

import java.util.ArrayList;

public class GraficoPie extends Grafico{
    private ArrayList<String> etiquetas;
    private ArrayList<Float> valores;
    private TipoPie tipoPie;
    private Float total;
    private String extra;

    public GraficoPie(TipoGrafico tipoGrafico, String tituloGrafico, Unir posicionesUnir,
                      ArrayList<String> etiquetas, ArrayList<Float> valores, TipoPie tipoPie, Float total, String extra){
        super(tipoGrafico, tituloGrafico, posicionesUnir);
        this.etiquetas = etiquetas;
        this.valores = valores;
        this.tipoPie = tipoPie;
        this.total = total;
        this.extra = extra;
    }

    public ArrayList<String> getEtiquetas() {
        return etiquetas;
    }

    public ArrayList<Float> getValores() {
        return valores;
    }

    public TipoPie getTipoPie() {
        return tipoPie;
    }

    public Float getTotal() {
        return total;
    }

    public String getExtra() {
        return extra;
    }

}
