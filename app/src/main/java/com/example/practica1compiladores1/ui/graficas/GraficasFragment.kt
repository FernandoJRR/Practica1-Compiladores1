package com.example.practica1compiladores1.ui.graficas

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.practica1compiladores1.R
import com.example.practica1compiladores1.databinding.FragmentGraficasBinding
import com.example.practica1compiladores1.modelo.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.DecimalFormat


class GraficasFragment : Fragment() {

    private var _binding: FragmentGraficasBinding? = null
    private var errores: Int = 0
    private var graficas: ArrayList<Grafico>? = null
    private var graficasEjecutadas: ArrayList<String>? = null

    private var barrasDataSet: ArrayList<BarDataSet> = ArrayList();

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GraficasViewModel::class.java)

        _binding = FragmentGraficasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if ((activity?.application as Globals).errores==null){
            binding.root.findViewById<TextView>(R.id.graficoTextView).text = "No se ha compilado ningun archivo"
        } else {
            errores = (activity?.application as Globals).errores!!.size
            if (errores > 0) {
                binding.root.findViewById<TextView>(R.id.graficoTextView).text = "Hay errores de compilacion, no se ha graficado nada\nRevisa el apartado de Reportes"
            } else {
                graficar()
            }
        }

        return root
    }

    private fun graficar(){
        graficasEjecutadas = (activity?.application as Globals).graficasEjecutar
        graficas = (activity?.application as Globals).graficasDefinidas
        for (ejecuciones in graficasEjecutadas!!){
            var graficoEjecutar: Grafico? = null
            for (grafica in graficas!!){
                if (ejecuciones.equals(grafica.titulo)){
                    graficoEjecutar = grafica
                    break
                }
            }
            graficar(graficoEjecutar!!)
        }
    }

    var labels: ArrayList<String> = ArrayList()
    private fun graficar(grafico: Grafico){
        if(grafico is GraficoBarras){
            var barras: BarChart = BarChart(this.context)
            var layoutSpec: ViewGroup.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,400)
            barras.layoutParams = layoutSpec

            labels.add(0,"item")
            var dataSet: BarDataSet = BarDataSet(getEntradasBarras(grafico),grafico.titulo)
            barrasDataSet.add(dataSet)
            var barData: BarData = BarData(dataSet)
            barData.barWidth = 0.3f
            barras.data = barData
            dataSet.colors = ColorTemplate.createColors(ColorTemplate.MATERIAL_COLORS)
            dataSet.valueTextColor = Color.BLACK
            dataSet.valueTextSize = 16f
            barras.description.isEnabled = false

            val xAxis: XAxis = barras.getXAxis()
            xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true

            var linearLayout: LinearLayout = binding.root.findViewById(R.id.linearLayout)
            linearLayout.addView(barras)

            // Reseteamos las labels para proxima grafica
            labels = ArrayList()
        } else if (grafico is GraficoPie) {
            var pie: PieChart = PieChart(this.context)
            var layoutSpec: ViewGroup.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,800)
            pie.layoutParams = layoutSpec

            var pieDataSet: PieDataSet = PieDataSet(getEntradasPie(grafico),grafico.titulo)
            pieDataSet.colors = ColorTemplate.createColors(ColorTemplate.MATERIAL_COLORS)
            pieDataSet.valueTextSize = 10f

            var pieData: PieData = PieData(pieDataSet)

            if (grafico.tipoPie == TipoPie.Porcentaje){
                pieData.setValueFormatter(MyValueFormatter())
                pieData.dataSet = pieDataSet
            }

            pie.data = pieData
            pie.description.isEnabled = false
            pie.invalidate()

            var linearLayout: LinearLayout = binding.root.findViewById(R.id.linearLayout)
            linearLayout.addView(pie)
        }
    }
    private fun getEntradasBarras(grafico: GraficoBarras): ArrayList<BarEntry>{
        var entradasBarras: ArrayList<BarEntry> = ArrayList()
        var uniones: ArrayList<Array<String>> = (activity?.application as Globals).interprete!!.interpretarGraficaBarras(grafico)

        var i = 0
        for (union in uniones){
            i++
            labels.add(union[0])
            entradasBarras.add(BarEntry(i.toFloat(),union[1].toFloat()))
        }

        return entradasBarras
    }

    private fun getEntradasPie(grafico: GraficoPie): ArrayList<PieEntry>{
        var entradasPie: ArrayList<PieEntry> = ArrayList()
        var uniones: ArrayList<Array<String>> = (activity?.application as Globals).interprete!!.interpretarGraficoPie(grafico)

        for (union in uniones){
            entradasPie.add(PieEntry(union[1].toFloat(),union[0]))
        }

        return entradasPie
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class MyValueFormatter : ValueFormatter() {
    private val mFormat: DecimalFormat
    override fun getFormattedValue(value: Float): String {
        return mFormat.format(value.toDouble()) + " %" // e.g. append percentage sign
    }

    init {
        mFormat = DecimalFormat("###,###,##0.0") // use one decimal
    }
}
