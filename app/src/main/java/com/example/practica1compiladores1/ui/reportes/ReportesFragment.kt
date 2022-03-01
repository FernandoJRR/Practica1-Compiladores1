package com.example.practica1compiladores1.ui.reportes

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.practica1compiladores1.R
import com.example.practica1compiladores1.analisis.CompilerError
import com.example.practica1compiladores1.analisis.OperadorEncontrado
import com.example.practica1compiladores1.databinding.FragmentReportesBinding
import com.example.practica1compiladores1.modelo.Globals
import com.example.practica1compiladores1.modelo.Grafico
import com.example.practica1compiladores1.modelo.GraficoBarras
import com.example.practica1compiladores1.modelo.GraficoPie


class ReportesFragment : Fragment() {

    private var _binding: FragmentReportesBinding? = null
    private var errores: ArrayList<CompilerError> = ArrayList()
    private var operacionesEncontradas: ArrayList<OperadorEncontrado> = ArrayList()
    private var graficasDefinidas: ArrayList<Grafico> = ArrayList()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(ReportesViewModel::class.java)

        _binding = FragmentReportesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if ((activity?.application as Globals).errores == null) {
            binding.root.findViewById<TextView>(R.id.reporteErroresText).text = "No se ha compilado ningun programa"
            binding.root.findViewById<TextView>(R.id.reporteOperadoresText).visibility = View.INVISIBLE
            binding.root.findViewById<TextView>(R.id.reporteGraficosText).visibility = View.INVISIBLE
            binding.root.findViewById<TableLayout>(R.id.reporteErroresTableLayout).visibility = View.INVISIBLE
            binding.root.findViewById<TableLayout>(R.id.reporteOperadoresTableLayout).visibility = View.INVISIBLE
            binding.root.findViewById<TableLayout>(R.id.reporteGraficosTableLayout).visibility = View.INVISIBLE
        } else {
            errores = (activity?.application as Globals).errores!!

            var erroresText = "Simbolo Posicion Tipo Descripcion\n"
            for (error in errores) {
                erroresText += error.simbolo+" "+error.posicion[0]+","+error.posicion[1]+" "+error.tipo+" "+error.descripcion+"\n"
            }

            createColumns()
            llenarData()
        }

        return root
    }

   fun createColumns() {
       //Se crean columnas para el reporte Errores
       var tableRow: TableRow = TableRow(this.context);
       tableRow.setLayoutParams(
           TableRow.LayoutParams (
                   TableRow.LayoutParams.FILL_PARENT,
           TableRow.LayoutParams.WRAP_CONTENT
       ));

       // Columna Simbolo
       var textViewSimbolo: TextView = TextView(this.context)
       textViewSimbolo.setText("Simbolo");
       textViewSimbolo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
       textViewSimbolo.setPadding(40, 5, 40, 0);
       tableRow.addView(textViewSimbolo);

       // Columna Posicion
       var textViewPosicion: TextView = TextView(this.context);
       textViewPosicion.setText("Posicion\n(Linea,Columna)");
       textViewPosicion.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
       textViewPosicion.setPadding(40, 5, 20, 0);
       tableRow.addView(textViewPosicion);

       // Columna Tipo
       var textViewTipo: TextView = TextView(this.context);
       textViewTipo.setText("Tipo");
       textViewTipo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
       textViewTipo.setPadding(20, 5, 20, 0);
       tableRow.addView(textViewTipo);

       //Columna Descripcion
       var textViewDescripcion: TextView = TextView(this.context);
       textViewDescripcion.setText("Descripcion");
       textViewDescripcion.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
       textViewDescripcion.setPadding(5, 5, 0, 0);
       tableRow.addView(textViewDescripcion);

       binding.root.findViewById<TableLayout>(R.id.reporteErroresTableLayout).addView(
           tableRow, TableLayout.LayoutParams(
               TableRow.LayoutParams.FILL_PARENT,
               TableRow.LayoutParams.WRAP_CONTENT
           )
       )

       //Se crean columnas para el reporte de Operadores
       tableRow = TableRow(this.context);
       tableRow.setLayoutParams(
           TableRow.LayoutParams (
               TableRow.LayoutParams.FILL_PARENT,
               TableRow.LayoutParams.WRAP_CONTENT
       ));

       // Columna Operador
       var textViewOperador: TextView = TextView(this.context);
       textViewOperador.setText("Operador");
       textViewOperador.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
       textViewOperador.setPadding(40, 5, 40, 0);
       tableRow.addView(textViewOperador);

       // Columna Posicion
       var textViewPosicionOperador: TextView = TextView(this.context);
       textViewPosicionOperador.setText("Posicion\n(Linea,Columna)");
       textViewPosicionOperador.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
       textViewPosicionOperador.setPadding(40, 5, 20, 0);
       tableRow.addView(textViewPosicionOperador);

       // Columna Ocurrencia
       var textViewOcurrencia: TextView = TextView(this.context)
       textViewOcurrencia.setText("Ocurrencia");
       textViewOcurrencia.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
       textViewOcurrencia.setPadding(40, 5, 20, 0);
       tableRow.addView(textViewOcurrencia);

       binding.root.findViewById<TableLayout>(R.id.reporteOperadoresTableLayout).addView(
           tableRow, TableLayout.LayoutParams(
               TableRow.LayoutParams.FILL_PARENT,
               TableRow.LayoutParams.WRAP_CONTENT
           )
       )

       //Se crean columnas para el reporte de Graficas
       tableRow = TableRow(this.context);
       tableRow.setLayoutParams(
           TableRow.LayoutParams (
               TableRow.LayoutParams.FILL_PARENT,
               TableRow.LayoutParams.WRAP_CONTENT
           ));

       // Columna Tipo Grafica
       var textViewTipoGrafica: TextView = TextView(this.context);
       textViewTipoGrafica.setText("Tipo");
       textViewTipoGrafica.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
       textViewTipoGrafica.setPadding(20, 5, 40, 0);
       tableRow.addView(textViewTipoGrafica);

       // Columna Cantidad
       var textViewCantidad: TextView = TextView(this.context);
       textViewCantidad.setText("Cantidad");
       textViewCantidad.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
       textViewCantidad.setPadding(40, 5, 20, 0);
       tableRow.addView(textViewCantidad);

       binding.root.findViewById<TableLayout>(R.id.reporteGraficosTableLayout).addView(
           tableRow, TableLayout.LayoutParams(
               TableRow.LayoutParams.FILL_PARENT,
               TableRow.LayoutParams.WRAP_CONTENT
           )
       )
   }

    fun llenarData() {
        if (errores.size > 0){
            binding.root.findViewById<TextView>(R.id.reporteOperadoresText).visibility = View.INVISIBLE
            binding.root.findViewById<TextView>(R.id.reporteGraficosText).visibility = View.INVISIBLE
            binding.root.findViewById<TableLayout>(R.id.reporteOperadoresTableLayout).visibility = View.INVISIBLE
            binding.root.findViewById<TableLayout>(R.id.reporteGraficosTableLayout).visibility = View.INVISIBLE
            for (error in errores) {
                var tableRow: TableRow = TableRow(this.context);
                tableRow.setLayoutParams(
                    TableRow.LayoutParams (
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    ));

                // Columna Simbolo
                var textViewSimbolo: TextView = TextView(this.context)
                textViewSimbolo.setText(error.simbolo);
                textViewSimbolo.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                textViewSimbolo.setPadding(40, 5, 40, 0);
                textViewSimbolo.setOnClickListener { view ->
                    Toast.makeText(this.context, (view as TextView).text,Toast.LENGTH_LONG).show()
                }

                tableRow.addView(textViewSimbolo);

                // Columna Posicion
                var textViewPosicion: TextView = TextView(this.context);
                textViewPosicion.setText(error.posicion[1].toString()+","+error.posicion[0].toString());
                textViewPosicion.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                textViewPosicion.setPadding(40, 5, 20, 0);
                textViewPosicion.setOnClickListener { view ->
                    Toast.makeText(this.context, (view as TextView).text,Toast.LENGTH_LONG).show()
                }
                tableRow.addView(textViewPosicion);

                // Columna Tipo
                var textViewTipo: TextView = TextView(this.context);
                textViewTipo.setText(error.tipo.toString());
                textViewTipo.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                textViewTipo.setPadding(20, 5, 20, 0);
                textViewTipo.setOnClickListener { view ->
                    Toast.makeText(this.context, (view as TextView).text,Toast.LENGTH_LONG).show()
                }
                tableRow.addView(textViewTipo);

                //Columna Descripcion
                var textViewDescripcion: TextView = TextView(this.context);
                textViewDescripcion.setText(error.descripcion);
                textViewDescripcion.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                textViewDescripcion.setPadding(5, 5, 0, 5);
                textViewDescripcion.width = 10

                textViewDescripcion.setOnClickListener { view ->
                    Toast.makeText(this.context, (view as TextView).text,Toast.LENGTH_LONG).show()
                }
                tableRow.addView(textViewDescripcion);

                binding.root.findViewById<TableLayout>(R.id.reporteErroresTableLayout).addView(
                    tableRow, TableLayout.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                )
            }
        } else {
            binding.root.findViewById<TextView>(R.id.reporteOperadoresText).visibility = View.VISIBLE
            binding.root.findViewById<TextView>(R.id.reporteGraficosText).visibility = View.VISIBLE
            binding.root.findViewById<TableLayout>(R.id.reporteOperadoresTableLayout).visibility = View.VISIBLE
            binding.root.findViewById<TableLayout>(R.id.reporteGraficosTableLayout).visibility = View.VISIBLE

            //Se indica la ausencia de errores
            var tableRow: TableRow = TableRow(this.context);
            tableRow.setLayoutParams(
                TableRow.LayoutParams (
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                ));


            var textViewError: TextView = TextView(this.context)
            textViewError.setText("No se detectaron errores");
            textViewError.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            textViewError.setPadding(40, 5, 40, 0);
            tableRow.addView(textViewError);
            binding.root.findViewById<TableLayout>(R.id.reporteErroresTableLayout).addView(
                tableRow, TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
            )

            operacionesEncontradas = (activity?.application as Globals).operadoresEncontrados!!
            //Se llena la informacion de operadores
            for (operador in operacionesEncontradas){
                tableRow = TableRow(this.context);
                tableRow.setLayoutParams(
                    TableRow.LayoutParams (
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                ));

                // Columna Operador
                var textViewOperador: TextView = TextView(this.context);
                textViewOperador.setText(operador.operador);
                textViewOperador.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                textViewOperador.setPadding(40, 5, 40, 0);
                tableRow.addView(textViewOperador);

                // Columna Posicion
                var textViewPosicionOperador: TextView = TextView(this.context);
                textViewPosicionOperador.setText(operador.posicion[0].toString()+","+operador.posicion[1].toString());
                textViewPosicionOperador.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                textViewPosicionOperador.setPadding(40, 5, 20, 0);
                tableRow.addView(textViewPosicionOperador);

                // Columna Ocurrencia
                var textViewOcurrencia: TextView = TextView(this.context)
                textViewOcurrencia.setText(operador.ocurrencia);
                textViewOcurrencia.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                textViewOcurrencia.setPadding(40, 5, 20, 0);
                tableRow.addView(textViewOcurrencia);

                binding.root.findViewById<TableLayout>(R.id.reporteOperadoresTableLayout).addView(
                    tableRow, TableLayout.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                )
            }

            graficasDefinidas = (activity?.application as Globals).graficasDefinidas!!
            //Se calcula la cantidad de graficas definidas por tipo
            var cantidadBarras: Int = 0
            var cantidadPie: Int = 0
            for (grafica in graficasDefinidas) {
                if (grafica is GraficoBarras){cantidadBarras++}
                if (grafica is GraficoPie){cantidadPie++}
            }

            tableRow = TableRow(this.context);
            tableRow.setLayoutParams(
                TableRow.LayoutParams (
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                ));


            var textViewTipoGrafica: TextView = TextView(this.context)
            textViewTipoGrafica.setText("Barras");
            textViewTipoGrafica.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            textViewTipoGrafica.setPadding(40, 5, 40, 0);
            tableRow.addView(textViewTipoGrafica);

            var textViewCantidadGrafica: TextView = TextView(this.context)
            textViewCantidadGrafica.setText(cantidadBarras.toString());
            textViewCantidadGrafica.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            textViewCantidadGrafica.setPadding(40, 5, 40, 0);
            tableRow.addView(textViewCantidadGrafica);

            binding.root.findViewById<TableLayout>(R.id.reporteGraficosTableLayout).addView(
                tableRow, TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
            )

            tableRow = TableRow(this.context);
            tableRow.setLayoutParams(
                TableRow.LayoutParams (
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                ));

            textViewTipoGrafica = TextView(this.context)
            textViewTipoGrafica.setText("Pie");
            textViewTipoGrafica.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            textViewTipoGrafica.setPadding(40, 5, 40, 0);
            tableRow.addView(textViewTipoGrafica);

            textViewCantidadGrafica = TextView(this.context)
            textViewCantidadGrafica.setText(cantidadPie.toString());
            textViewCantidadGrafica.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            textViewCantidadGrafica.setPadding(40, 5, 40, 0);
            tableRow.addView(textViewCantidadGrafica);

            binding.root.findViewById<TableLayout>(R.id.reporteGraficosTableLayout).addView(
                tableRow, TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}