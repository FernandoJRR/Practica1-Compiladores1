package com.example.practica1compiladores1.analisis;

import com.example.practica1compiladores1.modelo.Grafico;
import com.example.practica1compiladores1.modelo.GraficoBarras;
import com.example.practica1compiladores1.modelo.GraficoPie;
import com.example.practica1compiladores1.modelo.TipoGrafico;
import com.example.practica1compiladores1.modelo.TipoPie;
import com.example.practica1compiladores1.modelo.Unir;

import java.util.ArrayList;
import java.util.Collections;

public class Interprete {
    private ArrayList<CompilerError> errores = new ArrayList<>();
    //Representan todas las graficas que tienen todos sus atributos definidos, aunque sean inconsistentes
    private ArrayList<Grafico> graficasCompletas = new ArrayList<>();
    //Representan todas las graficas que no presentan errores semanticos
    private ArrayList<Grafico> graficasCorrectas = new ArrayList<>();
    private ArrayList<String> graficasEjecutar = new ArrayList<>();
    private Parser parser;

    public Parser getParser() {
        return parser;
    }

    public ArrayList<CompilerError> getErrores() {
        return errores;
    }

    public ArrayList<String> getGraficasEjecutar() {
        return graficasEjecutar;
    }

    public void setErrores(ArrayList<CompilerError> errores) {
        this.errores = errores;
    }

    public ArrayList<Grafico> getGraficasCorrectas() {
        return graficasCorrectas;
    }

    public Interprete(Parser parser){
        this.parser = parser;
    }

    public void analisis(){
        try {
            parser.debug_parse();
        } catch (Exception e){
            agregarError("Ha ocurrido un error en el analisis sintactico","EOF");
        }
        try {
            analisisSemantico(parser.getGraficasDefinidas());
        } catch (Exception e) {
            agregarError("Ha ocurrido un error en el analisis semantico", "EOF");
        }

    }

    private void analisisSemantico(ArrayList<Grafico> graficosDefinidos){
        checkNullity(graficosDefinidos);
        if (!checkEjecutar(graficasCompletas, parser.getEjecucionesGraficos())){
            Collections.reverse(parser.getEjecucionesGraficos());
            graficasEjecutar = parser.getEjecucionesGraficos();
        }
        checkInconsistency(graficasCompletas);
    }

    private void checkNullity(ArrayList<Grafico> graficosDefinidos){
        for (Grafico grafico : graficosDefinidos) {
            int erroresGrafico = 0;
            if (grafico.getTitulo() == null || grafico.getTitulo().toString() == null) {
                agregarError("TItulo del grafico no definido", "titulo"); erroresGrafico++;
                if (grafico.getUnir() == null||grafico.getUnir().getParPosiciones() == null) {agregarError("Atributo unir no definido", "unir:");erroresGrafico++;}
                if (grafico instanceof GraficoBarras) {
                    if(((GraficoBarras) grafico).getEjeX()==null || ((GraficoBarras) grafico).getEjeX().toString()==null){agregarError("Atributo eje X no definido", "ejex");erroresGrafico++;}
                    if(((GraficoBarras) grafico).getEjeY()==null||((GraficoBarras) grafico).getEjeY().toString()==null){agregarError("Atributo eje Y no definido", "ejey");erroresGrafico++;}

                } else if(grafico instanceof GraficoPie){
                    if(((GraficoPie) grafico).getEtiquetas()==null||((GraficoPie) grafico).getEtiquetas().toString()==null){agregarError("Atributo etiquetas no definido", "etiquetas");erroresGrafico++;}
                    if(((GraficoPie) grafico).getValores()==null||((GraficoPie) grafico).getValores().toString()==null){agregarError("Atributo valores no definido", "valores");erroresGrafico++;}

                    //Se comprueba el estado que tienen el tipo de pie y el atributo de total
                    if(((GraficoPie) grafico).getTipoPie()==null||((GraficoPie) grafico).getTipoPie().toString()==null){
                        agregarError("Atributo tipo de pie no definido", "tipo");erroresGrafico++;
                    } else if(((GraficoPie) grafico).getTipoPie()==TipoPie.Cantidad) {
                        if(((GraficoPie) grafico).getTotal()==null){agregarError("Atributo total no definido, cuando deberia", "total");erroresGrafico++;}
                    } else if(((GraficoPie) grafico).getTipoPie()==TipoPie.Porcentaje){
                        if(((GraficoPie) grafico).getTotal()!=null){agregarError("Atributo total definido, cuando no deberia", "total");erroresGrafico++;}
                    }
                    if(((GraficoPie) grafico).getExtra()==null||((GraficoPie) grafico).getExtra().toString()==null){agregarError("Atributo extra no definido", "extra");erroresGrafico++;}
                }
            } else {
                String titulo = grafico.getTitulo();
                if (grafico.getUnir()==null||grafico.getUnir().getParPosiciones() == null) {agregarError("Atributo unir de grafica \'"+titulo+"\', no definido", "unir:");erroresGrafico++;}
                if (grafico instanceof GraficoBarras) {
                    if(((GraficoBarras) grafico).getEjeX()==null||((GraficoBarras) grafico).getEjeX().toString()==null){agregarError("Atributo eje X de grafica \'"+titulo+"\', no definido", "ejex");erroresGrafico++;}
                    if(((GraficoBarras) grafico).getEjeY()==null||((GraficoBarras) grafico).getEjeY().toString()==null){agregarError("Atributo eje Y de grafica \'"+titulo+"\', no definido", "ejey");erroresGrafico++;}

                } else if(grafico instanceof GraficoPie){
                    if(((GraficoPie) grafico).getEtiquetas()==null||((GraficoPie) grafico).getEtiquetas().toString()==null){agregarError("Atributo etiquetas de grafica \'"+titulo+"\', no definido", "etiquetas");erroresGrafico++;}
                    if(((GraficoPie) grafico).getValores()==null||((GraficoPie) grafico).getValores().toString()==null){agregarError("Atributo valores de grafica \'"+titulo+"\', no definido", "valores");erroresGrafico++;}

                    //Se comprueba el estado que tienen el tipo de pie y el atributo de total
                    if(((GraficoPie) grafico).getTipoPie()==null||((GraficoPie) grafico).getTipoPie().toString()==null){
                        agregarError("Atributo tipo de pie de grafica \'"+titulo+"\', no definido", "tipo");erroresGrafico++;
                    } else if(((GraficoPie) grafico).getTipoPie()==TipoPie.Cantidad) {
                        if(((GraficoPie) grafico).getTotal()==null||((GraficoPie) grafico).getTotal().toString()==null){agregarError("Atributo total de grafica \'"+titulo+"\', no definido, cuando deberia", "total");erroresGrafico++;}
                    } else if(((GraficoPie) grafico).getTipoPie()==TipoPie.Porcentaje){
                        if(((GraficoPie) grafico).getTotal()!=null){agregarError("Atributo total de grafica \'"+titulo+"\', definido, cuando no deberia", "total");erroresGrafico++;}
                    }
                    if(((GraficoPie) grafico).getExtra()==null||((GraficoPie) grafico).getExtra().toString()==null){agregarError("Atributo extra de grafica \'"+titulo+"\', no definido", "extra");erroresGrafico++;}
                }
            }
            if (erroresGrafico==0) {graficasCompletas.add(grafico);}
            erroresGrafico = 0;
        }
    }

    private void checkInconsistency(ArrayList<Grafico> graficasCompletas){
        for (Grafico grafico : graficasCompletas) {
            int erroresGrafica = 0;
            if (checkUnir(grafico)) {erroresGrafica++;}
            if (grafico instanceof GraficoBarras) {
                if(((GraficoBarras) grafico).getEjeX()==null){agregarError("Atributo eje X no definido", "ejex");}
                if(((GraficoBarras) grafico).getEjeY()==null){agregarError("Atributo eje Y no definido", "ejey");}
            } else if(grafico instanceof GraficoPie){
                if (checkTotal((GraficoPie)grafico)) {erroresGrafica++;}
                if (checkValores((GraficoPie)grafico)) {erroresGrafica++;}
            }
            if (erroresGrafica==0) {graficasCorrectas.add(grafico);}
            erroresGrafica = 0;
        }
    }

    private boolean checkValores(GraficoPie graficoPie){
        ArrayList<Float> valores = graficoPie.getValores();
        float totalValores = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            totalValores = (float)valores.stream().mapToDouble(Float::doubleValue).sum();
        }
        TipoPie tipoPie = graficoPie.getTipoPie();
        for (Float valor : valores) {
            if (valor<0) {
                agregarError("Valor ingresado en grafica "+graficoPie.getTitulo()+" es invalido", valor.toString());
                return true;
            }
        }
        if ((tipoPie == TipoPie.Porcentaje)&&(totalValores > 360f)) {
            agregarError("El total de los valores en la en grafica "+graficoPie.getTitulo()+" excede el 100%(360 grados)", "valores");
            return true;
        } else if((tipoPie == TipoPie.Cantidad)&&(totalValores > graficoPie.getTotal())){
            agregarError("El total de los valores en la en grafica "+graficoPie.getTitulo()+" excede el valor del total definido", "valores");
            return true;
        }
        return false;
    }

    private boolean checkTotal(GraficoPie graficoPie){
        if (graficoPie.getTipoPie() == TipoPie.Cantidad) {
            if(graficoPie.getTotal()<0){
                agregarError("El total definido es invalido", graficoPie.getTotal().toString());
                return true;
            }
        }
        return false;
    }

    private boolean checkUnir(Grafico grafico){
        Unir listaTuplas = grafico.getUnir();
        int totalEjeXEtiquetas = (Integer) (grafico.getTipoGrafico() == TipoGrafico.Barras? ((GraficoBarras)grafico).getEjeX().size():((GraficoPie)grafico).getEtiquetas().size());
        int totalEjeYValores = (Integer) (grafico.getTipoGrafico() == TipoGrafico.Barras? ((GraficoBarras)grafico).getEjeY().size():((GraficoPie)grafico).getValores().size());

        for (float[] tupla : listaTuplas.getParPosiciones()) {
            try {
                boolean error = false;
                int valorTupla0 = (int)tupla[0];
                int valorTupla1 = (int)tupla[1];
                if (valorTupla0 >= totalEjeXEtiquetas) {
                    agregarError("El valor en la tupla es mayor los datos ejex/etiqueta existentes"+grafico.getTitulo(), String.valueOf(valorTupla0));
                    error = true;
                }
                if (valorTupla1 >= totalEjeYValores) {
                    agregarError("El valor en la tupla es mayor los datos ejey/valores existentes"+grafico.getTitulo(), String.valueOf(valorTupla1));
                    error = true;
                }
                if (error) {return true;}
            } catch (Exception e) {
                agregarError("Los valores de la tupla deben de ser enteros"+grafico.getTitulo(), "{"+tupla[0]+","+tupla[1]+"}");
                return true;
            }
        }
        return false;
    }

    private boolean checkEjecutar(ArrayList<Grafico> graficosCompletos, ArrayList<String> ejecuciones){
        for (String ejecucion : ejecuciones) {
            boolean existe = false;
            for (Grafico grafico : graficosCompletos) {
                if (grafico.getTitulo().equals(ejecucion)){
                    existe = true;
                }
            }
            if (!existe){
                agregarError("El grafico a ejecutar no ha sido definido",ejecucion);
                return true;
            }
        }
        return false;
    }

    public ArrayList<String[]> interpretarGraficaBarras(GraficoBarras grafico){
        ArrayList<String[]> uniones = new ArrayList<>();
        Unir union = grafico.getUnir();

        for (float[] tupla : union.getParPosiciones()) {
            int ejeX = (int)tupla[0];
            int ejeY = (int)tupla[1];

            String identificadorEjeX = grafico.getEjeX().get(ejeX);
            String valorEjeY = grafico.getEjeY().get(ejeY).toString();

            uniones.add(new String[]{identificadorEjeX,valorEjeY});
        }

        return uniones;
    }

    public ArrayList<String[]> interpretarGraficoPie(GraficoPie grafico){
        ArrayList<String[]> uniones = new ArrayList<>();
        Unir union = grafico.getUnir();
         float totalUnion = 0;
        for (float[] tupla : union.getParPosiciones()) {
            int etiquetaPos = (int)tupla[0];
            int valorPos = (int)tupla[1];

            String etiqueta = grafico.getEtiquetas().get(etiquetaPos);
            Float valor = grafico.getValores().get(valorPos);
            totalUnion += valor;
            if (grafico.getTipoPie() == TipoPie.Porcentaje){
                valor = (valor*100f)/360f;
            }

            uniones.add(new String[]{etiqueta, valor.toString()});
        }

        //Se checkea si es necesario agregar el atributo de extra al grafico
        if (grafico.getTipoPie() == TipoPie.Cantidad){
            if (totalUnion < grafico.getTotal()){
                uniones.add(new String[]{grafico.getExtra(),((Float)(grafico.getTotal()-totalUnion)).toString()});
            }
        } else if (grafico.getTipoPie() == TipoPie.Porcentaje){
            if (totalUnion < 360f){
                uniones.add(new String[]{grafico.getExtra(),((Float)(100f*(360f-totalUnion)/360f)).toString()});
            }
        }
        return uniones;
    }

    private void agregarError(String mensajeError, String simbolo){
        errores.add(new CompilerError(simbolo, new int[]{0,0}, TipoError.Semantico, mensajeError));
    }

}
