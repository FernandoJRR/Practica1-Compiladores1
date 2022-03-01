package com.example.practica1compiladores1.analisis;

import java_cup.runtime.Symbol;
import java.util.ArrayList;
%%

%class Lexer
%public
%type java_cup.runtime.Symbol
%cup
%unicode
%line
%column
%state COMENTARIO
L=[a-zA-Z_]+
D=[0-9]+
espacio=[\s\t\n\r]+

%{
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }

    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private ArrayList<CompilerError> errores = new ArrayList<>();

    public void setErrores(ArrayList<CompilerError> errores){
        this.errores = errores;
    }

    private void agregarError(String mensajeError){
        errores.add(new CompilerError(yytext(),new int[]{yyline+1, yycolumn+1},TipoError.Lexico,mensajeError));
    }
%}

%eofval{
      return new Symbol(sym.EOF, yycolumn+1, yyline+1, null);
%eofval}

%%

<YYINITIAL> {
    //Se declaran las palabras reservadas
    Def|def {return new Symbol(sym.P_R_Def, yycolumn+1, yyline+1, yytext());}
    Barras|Pie {return new Symbol(sym.P_R_TipoGrafico, yycolumn+1, yyline+1, yytext());}
    titulo {return new Symbol(sym.P_R_Titulo, yycolumn+1, yyline+1, yytext());}
    ejex {return new Symbol(sym.P_R_EjeX, yycolumn+1, yyline+1, yytext());}
    ejey {return new Symbol(sym.P_R_EjeY, yycolumn+1, yyline+1, yytext());}
    etiquetas {return new Symbol(sym.P_R_Etiquetas, yycolumn, yyline+1, yytext());}
    valores {return new Symbol(sym.P_R_Valores, yycolumn+1, yyline+1, yytext());}
    unir {return new Symbol(sym.P_R_Unir, yycolumn+1, yyline+1, yytext());}
    tipo {return new Symbol(sym.P_R_Tipo, yycolumn+1, yyline+1, yytext());}
    total {return new Symbol(sym.P_R_Total, yycolumn+1, yyline+1, yytext());}
    extra {return new Symbol(sym.P_R_Extra, yycolumn+1, yyline+1, yytext());}
    Cantidad {return new Symbol(sym.T_G_Cantidad, yycolumn+1, yyline+1, yytext());}
    Porcentaje {return new Symbol(sym.T_G_Porcentajes, yycolumn+1, yyline+1, yytext());}
    Ejecutar {return new Symbol(sym.P_R_Ejecutar, yycolumn+1, yyline+1, yytext());}

    //Se declaran simbolos
    "\"" {return new Symbol(sym.Sim_Comillas, yycolumn+1, yyline+1, yytext());}
    "." {return new Symbol(sym.Sim_Punto, yycolumn+1, yyline+1, yytext());}
    "," {return new Symbol(sym.Sim_Coma, yycolumn+1, yyline+1, yytext());}
    ":" {return new Symbol(sym.Sim_DPuntos, yycolumn+1, yyline+1, yytext());}
    ";" {return new Symbol(sym.Sim_PComa, yycolumn+1, yyline+1, yytext());}
    "+" {return new Symbol(sym.Oper_Suma, yycolumn+1, yyline+1, yytext());}
    "-" {return new Symbol(sym.Oper_Resta, yycolumn+1, yyline+1, yytext());}
    "*" {return new Symbol(sym.Oper_Multi, yycolumn+1, yyline+1, yytext());}
    "/" {return new Symbol(sym.Oper_Div, yycolumn+1, yyline+1, yytext());}
    "(" {return new Symbol(sym.Par_Izq, yycolumn+1, yyline+1, yytext());}
    ")" {return new Symbol(sym.Par_Der, yycolumn+1, yyline+1, yytext());}
    "{" {return new Symbol(sym.Llave_Izq, yycolumn+1, yyline+1, yytext());}
    "}" {return new Symbol(sym.Llave_Der, yycolumn+1, yyline+1, yytext());}
    "[" {return new Symbol(sym.Corch_Izq, yycolumn+1, yyline+1, yytext());}
    "]" {return new Symbol(sym.Corch_Der, yycolumn+1, yyline+1, yytext());}

    "\""({L}|{D}|[\s\t])*"\"" {return new Symbol(sym.Cadena, yycolumn+1, yyline+1, yytext());}
    "-"?{D}+ {return new Symbol(sym.Entero, yycolumn+1, yyline+1, yytext());}
    "-"?{D}+"."{D}+ {return new Symbol(sym.Decimal, yycolumn+1, yyline+1, yytext());}

    //Se ignoraran espacios' sueltos
    {espacio}+ {/*Ignorar*/}

    "#" {yybegin(COMENTARIO);}
    ({L}|{D})+ {agregarError("Palabra no definida en el lenguaje");}
    [^] {agregarError("Caracter no definido en el lenguaje");}
}

<COMENTARIO> {
    ("\n"|"\r") {yybegin(YYINITIAL);}
    [^] {/*Ignorar*/}
}

