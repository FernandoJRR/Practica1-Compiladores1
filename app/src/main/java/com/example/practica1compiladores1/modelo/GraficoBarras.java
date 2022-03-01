package com.example.practica1compiladores1.modelo;

import java.util.ArrayList;

public class GraficoBarras extends Grafico{
    private ArrayList<String> ejeX;
    private ArrayList<Float> ejeY;

    public GraficoBarras(TipoGrafico tipoGrafico, String tituloGrafico, Unir posicionesUnir, ArrayList<String> ejeX, ArrayList<Float> ejeY){
        super(tipoGrafico, tituloGrafico, posicionesUnir);
        this.ejeX = ejeX;
        this.ejeY = ejeY;
    }

    public ArrayList<String> getEjeX() {
        return ejeX;
    }

    public ArrayList<Float> getEjeY() {
        return ejeY;
    }

}
