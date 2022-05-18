package com.xcheko51x.adminmenurestaurantapp

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.xcheko51x.adminmenurestaurantapp.Adaptadores.AdaptadorCategorias
import com.xcheko51x.adminmenurestaurantapp.Clases.Categoria
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class AdminCategorias : AppCompatActivity() {

    lateinit var listaCategorias: ArrayList<Categoria>
    lateinit var adaptadorCategoria: AdaptadorCategorias
    lateinit var rvCategorias: RecyclerView
    lateinit var fab: FloatingActionButton

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_categorias)

        rvCategorias = findViewById(R.id.rvCategorias)
        rvCategorias.layoutManager = GridLayoutManager(this, 2)

        fab = findViewById(R.id.fab)

        //listaCategorias = MetodosVolley(this).obtenerListaCategoria()
        listaCategorias = MetodosVolley(this).obtenerListaCategoria()
        adaptadorCategoria = AdaptadorCategorias(this, listaCategorias)
        rvCategorias.adapter = adaptadorCategoria

        fab.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            // Get the layout inflater
            val inflater = this.layoutInflater

            var vistaAlertDialog = inflater.inflate(R.layout.alert_dialog_add_edit_categoria, null)

            var tvAccion = vistaAlertDialog.findViewById<TextView>(R.id.tvTitulo)
            tvAccion.text = "AGREGAR CATEGORIA"

            var etNomCategoria = vistaAlertDialog.findViewById<EditText>(R.id.etNomCategoria)

            builder.setView(vistaAlertDialog)
                // Add action buttons
                .setPositiveButton("ACEPTAR",
                    DialogInterface.OnClickListener { dialog, id ->
                        agregarCategoria(etNomCategoria.text.toString())
                        finish()
                        startActivity(intent)
                    })
                .setNegativeButton("CANCELAR",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            builder.create()
            builder.setCancelable(false)
            builder.show()
        }
    }

    fun agregarCategoria(nomCategoria: String) {
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
                parametros["accion"] = "203"
                parametros["nomCategoria"] = nomCategoria.lowercase()

                return parametros
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}