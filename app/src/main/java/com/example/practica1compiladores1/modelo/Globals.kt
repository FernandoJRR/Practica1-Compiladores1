package com.example.practica1compiladores1.modelo

import android.app.Application
import com.example.practica1compiladores1.analisis.CompilerError
import com.example.practica1compiladores1.analisis.Interprete
import com.example.practica1compiladores1.analisis.OperadorEncontrado

class Globals : Application() {
    var errores: ArrayList<CompilerError>? = null
    var operadoresEncontrados: ArrayList<OperadorEncontrado>? = null
    var graficasDefinidas: ArrayList<Grafico>? = null
    var graficasEjecutar: ArrayList<String>? = null
    var interprete: Interprete? = null
}