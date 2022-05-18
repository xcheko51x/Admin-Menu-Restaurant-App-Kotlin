package com.xcheko51x.adminmenurestaurantapp

import android.content.Context
import android.content.DialogInterface
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.xcheko51x.adminmenurestaurantapp.Clases.Categoria
import com.xcheko51x.adminmenurestaurantapp.Clases.Platillo
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MetodosVolley {

    var context: Context

    constructor(context: Context) {
        this.context = context
    }

    fun obtenerListaSpinnerCategoria(): ArrayList<String> {
        val listaSpinnerCategoria = ArrayList<String>()

        if(listaSpinnerCategoria.size == 0) {
            listaSpinnerCategoria.add("Selecciona una categoria")
        }

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, context.resources.getString(R.string.URL_API),
            Response.Listener { response ->

                try {

                    var jsonObject = JSONObject(response)
                    var jsonArray = jsonObject.getJSONArray("categorias")

                    for (i in 0..jsonArray.length()) {
                        var jsonObject1 = jsonArray.getJSONObject(i)

                        listaSpinnerCategoria.add(
                            jsonObject1.getString("nomCategoria")
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(context, "Ocurrio error", Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                val parametros: MutableMap<String, String> = Hashtable<String, String>()
                parametros["accion"] = "205"
                return parametros
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

        return listaSpinnerCategoria
    }

    fun obtenerListaCategoria(): ArrayList<Categoria> {
        val listaCategorias = ArrayList<Categoria>()

        // En este apartado se programa lo que deseamos hacer en caso de no haber errores
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, context.resources.getString(R.string.URL_API),
            Response.Listener { response ->

                try {

                    var jsonObject = JSONObject(response)
                    var jsonArray = jsonObject.getJSONArray("categorias")

                    for (i in 0..jsonArray.length()) {
                        var jsonObject1 = jsonArray.getJSONObject(i)

                        listaCategorias.add(
                            Categoria(
                                jsonObject1.getString("imagenCategoria"),
                                jsonObject1.getString("nomCategoria")
                            )
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(context, "Ocurrio error", Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                val parametros: MutableMap<String, String> = Hashtable<String, String>()
                parametros["accion"] = "201"
                return parametros
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

        return listaCategorias
    }

    fun obtenerListaPlatillos(): ArrayList<Platillo> {
        val listaPlatillo = ArrayList<Platillo>()

        // En este apartado se programa lo que deseamos hacer en caso de no haber errores
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, context.resources.getString(R.string.URL_API),
            Response.Listener { response ->

                try {

                    var jsonObject = JSONObject(response)
                    var jsonArray = jsonObject.getJSONArray("platillos")

                    for (i in 0..jsonArray.length()) {
                        var jsonObject1 = jsonArray.getJSONObject(i)

                        listaPlatillo.add(
                            Platillo(
                                jsonObject1.getString("nombrePlatillo"),
                                jsonObject1.getString("descripcionPlatillo"),
                                jsonObject1.getString("precio"),
                                jsonObject1.getString("categoria")
                            )
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(context, "Ocurrio error", Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                val parametros: MutableMap<String, String> = Hashtable<String, String>()
                parametros["accion"] = "302"
                return parametros
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)

        return listaPlatillo
    }
}