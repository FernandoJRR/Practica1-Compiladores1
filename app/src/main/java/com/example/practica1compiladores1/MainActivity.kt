package com.example.practica1compiladores1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.practica1compiladores1.analisis.CompilerError
import com.example.practica1compiladores1.analisis.Interprete
import com.example.practica1compiladores1.analisis.Lexer
import com.example.practica1compiladores1.analisis.Parser
import com.example.practica1compiladores1.databinding.ActivityMainBinding
import com.example.practica1compiladores1.modelo.Globals
import com.example.practica1compiladores1.ui.graficas.GraficasFragment
import com.example.practica1compiladores1.ui.graficas.GraficasViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text
import java.io.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var selectedfile: Uri? = null
    private var actualView: View? = null
    private var erroresText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            var errores: ArrayList<CompilerError> = ArrayList();
            val multilinea: TextView = findViewById(R.id.editTextTextMultiLine)
            var reader: Reader = StringReader(multilinea.text.toString())
            var lexer: Lexer = Lexer(reader)
            lexer.setErrores(errores)
            var parser: Parser = Parser(lexer)
            parser.errores = errores
            var interprete: Interprete = Interprete(parser)
            interprete.setErrores(errores)
            interprete.analisis()

            (application as Globals).errores = errores
            (application as Globals).operadoresEncontrados = interprete.parser.listaOperaciones
            (application as Globals).graficasDefinidas = interprete.graficasCorrectas
            (application as Globals).graficasEjecutar = interprete.graficasEjecutar
            (application as Globals).interprete = interprete

            Snackbar.make(view, "El archivo ha sido compilado, revisa los apartados de Graficas y Reportes para mas detalles", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        binding.appBarMain.fab2.setOnClickListener () {
            actualView = it
            openFile()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_editor, R.id.nav_graficas, R.id.nav_reportes
            ), drawerLayout
        )


        val listener = navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id != R.id.nav_editor){
                binding.appBarMain.fab.visibility = View.INVISIBLE
                binding.appBarMain.fab2.visibility = View.INVISIBLE
            } else {
                binding.appBarMain.fab.visibility = View.VISIBLE
                binding.appBarMain.fab2.visibility = View.VISIBLE
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }
    @Throws(IOException::class)
    private fun openFile() {
        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 123)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == RESULT_OK) {
            selectedfile = data?.data //The uri with the location of the file
        }

        val stringBuilder = StringBuilder()
        selectedfile?.let {
            contentResolver.openInputStream(it)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    var line: String? = reader.readLine()
                    while (line != null) {
                        stringBuilder.append(line+"\n")
                        line = reader.readLine()
                    }
                }
            }
        }
        val multilinea: TextView = findViewById(R.id.editTextTextMultiLine)
        multilinea.text = stringBuilder.toString()

        actualView?.let {
            Snackbar.make(it, "Archivo abierto", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

}