package com.example.practica1compiladores1.ui.graficas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GraficasViewModel : ViewModel() {

    private var _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }

    private var _textoErrores = MutableLiveData<String>().apply {
        value = "Errores"
    }
    val text: LiveData<String> = _text
    val textoErrores: LiveData<String> = _textoErrores
}