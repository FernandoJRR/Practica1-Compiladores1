package com.example.practica1compiladores1.modelo;

import java.util.ArrayList;

public class Unir {
    private ArrayList<float[]> paresPosiciones;

    public Unir(ArrayList<float[]> paresPosiciones){
        this.paresPosiciones = paresPosiciones;
    }

    public ArrayList<float[]> getParPosiciones() {
        return paresPosiciones;
    }

    public String toString(){
        String cadena = "[";
        for (float[] tupla : this.paresPosiciones) {
            cadena += "{"+tupla[0]+","+tupla[1]+"}";
        }
        cadena += "]";
        return cadena;
    }

}
