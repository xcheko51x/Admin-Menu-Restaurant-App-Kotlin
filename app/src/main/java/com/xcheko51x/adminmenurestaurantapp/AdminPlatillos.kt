package com.xcheko51x.adminmenurestaurantapp

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.xcheko51x.adminmenurestaurantapp.Adaptadores.AdaptadorPlatillos
import com.xcheko51x.adminmenurestaurantapp.Clases.Platillo
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class AdminPlatillos : AppCompatActivity() {

    lateinit var rvPlatillos: RecyclerView
    lateinit var listaPlatillos: ArrayList<Platillo>
    lateinit var adaptadorPlatillos: AdaptadorPlatillos
    lateinit var fab: FloatingActionButton
    var auxSpinnerPosition: Int = 0
    lateinit var auxSpinnerText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_platillos)

        rvPlatillos = findViewById(R.id.rvPlatillos)
        rvPlatillos.layoutManager = GridLayoutManager(this, 1)

        fab = findViewById(R.id.fab)

        listaPlatillos = MetodosVolley(this).obtenerListaPlatillos()
        adaptadorPlatillos = AdaptadorPlatillos(this, listaPlatillos)
        rvPlatillos.adapter = adaptadorPlatillos

        fab.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            // Get the layout inflater
            val inflater = this.layoutInflater

            var vistaAlertDialog = inflater.inflate(R.layout.alert_dialog_add_edit_platillo, null)

            var tvAccion = vistaAlertDialog.findViewById<TextView>(R.id.tvTitulo)
            tvAccion.text = "AGREGAR PLATILLO"
            var etNomPlatillo = vistaAlertDialog.   findViewById<TextView>(R.id.etNomPlatillo)
            var etDescripcionPlatillo = vistaAlertDialog.findViewById<TextView>(R.id.etDescripcionPlatillo)

            var spiCategoria = vistaAlertDialog.findViewById<Spinner>(R.id.spiCategorias)
            var listaSpinner = ArrayList<String>()

            var etPrecio = vistaAlertDialog.findViewById<EditText>(R.id.etPrecio)

            listaSpinner = MetodosVolley(this).obtenerListaSpinnerCategoria()

            spiCategoria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaSpinner)

            spiCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    auxSpinnerText = "Selecciona una categoria"
                }

                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    auxSpinnerPosition = position
                    auxSpinnerText = parent.getItemAtPosition(position).toString()
                }
            }

            builder.setView(vistaAlertDialog)
                // Add action buttons
                .setPositiveButton("ACEPTAR",
                    DialogInterface.OnClickListener { dialog, id ->
                        if(auxSpinnerPosition == 0) {
                            Toast.makeText(this, "Tienes que seleccionar una categoria", Toast.LENGTH_LONG).show()
                        } else {

                            agregarPlatillo(
                                etNomPlatillo.text.toString(),
                                etDescripcionPlatillo.text.toString(),
                                etPrecio.text.toString(),
                                auxSpinnerText
                            )
                            finish()
                            startActivity(intent)
                        }
                    })
                .setNegativeButton("CANCELAR",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            builder.create()
            builder.setCancelable(false)
            builder.show()
        }
    }

    fun agregarPlatillo(
        etNomPlatillo: String,
        etDescripcionPlatillo: String,
        etPrecio: String,
        spiCategoriaSelect: String
    ) {

        // En este apartado se programa lo que deseamos hacer en caso de no haber errores
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, resources.getString(R.string.URL_API),
            Response.Listener { response ->

                try {

                    var jsonObject = JSONObject(response)
                    if(jsonObject.getString("codigo").equals("OK")) {
                        Toast.makeText(this, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(this, "Ocurrio error", Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                val parametros: MutableMap<String, String> = Hashtable<String, String>()
                parametros["accion"] = "303"
                parametros["nomPlatillo"] = etNomPlatillo
                parametros["descPlatillo"] = etDescripcionPlatillo
                parametros["precio"] = etPrecio
                parametros["categoria"] = spiCategoriaSelect
                return parametros
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}