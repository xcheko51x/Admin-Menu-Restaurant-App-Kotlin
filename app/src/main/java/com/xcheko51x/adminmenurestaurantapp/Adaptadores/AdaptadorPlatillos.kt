package com.xcheko51x.adminmenurestaurantapp.Adaptadores

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.xcheko51x.adminmenurestaurantapp.AdminPlatillos
import com.xcheko51x.adminmenurestaurantapp.Clases.Categoria
import com.xcheko51x.adminmenurestaurantapp.Clases.Platillo
import com.xcheko51x.adminmenurestaurantapp.MetodosVolley
import com.xcheko51x.adminmenurestaurantapp.R
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class AdaptadorPlatillos(
    val context: Context,
    var listaPlatillos: ArrayList<Platillo>
): RecyclerView.Adapter<AdaptadorPlatillos.ViewHolder>() {

    var rutaImagenes = context.resources.getString(R.string.RUTA_IMAGENES)
    var auxSpinnerPosition: Int = 0
    lateinit var auxSpinnerText: String

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdaptadorPlatillos.ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_platillos, parent, false)
        return AdaptadorPlatillos.ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: AdaptadorPlatillos.ViewHolder, position: Int) {

        holder.tvNombrePlatillo.text = listaPlatillos.get(position).nombrePlatillo.uppercase()
        holder.tvCategoria.text = listaPlatillos.get(position).categoria.uppercase()

        if(
            listaPlatillos.get(position).descripcionPlatillo.isNullOrEmpty() ||
            listaPlatillos.get(position).descripcionPlatillo.equals("") ||
            listaPlatillos.get(position).descripcionPlatillo.equals("null")
        ) {
            holder.tvDescripcion.visibility = View.GONE
        } else {
            holder.tvDescripcion.visibility = View.VISIBLE
            holder.tvDescripcion.text = listaPlatillos.get(position).descripcionPlatillo.uppercase()
        }

        holder.precio.text = "$${listaPlatillos.get(position).precio}"

        holder.ivEditarPlatillo.setOnClickListener {
            formEditarPlatillo(listaPlatillos.get(position).nombrePlatillo.toString(), position)
        }

        holder.ivBorrarPlatillo.setOnClickListener {
            borrarPlatillo(listaPlatillos, position)
        }
    }

    fun formEditarPlatillo(nomPlatillo: String, position: Int) {
        val builder = AlertDialog.Builder(context)

        var vistaAlertDialog = LayoutInflater.from(context).inflate(R.layout.alert_dialog_add_edit_platillo, null)

        var tvAccion = vistaAlertDialog.findViewById<TextView>(R.id.tvTitulo)
        tvAccion.text = "EDITAR PLATILLO"

        var etNomPlatillo = vistaAlertDialog.findViewById<EditText>(R.id.etNomPlatillo)
        etNomPlatillo.setText(listaPlatillos.get(position).nombrePlatillo)

        var etDescripcionPlatillo = vistaAlertDialog.findViewById<EditText>(R.id.etDescripcionPlatillo)

        if (
            listaPlatillos.get(position).descripcionPlatillo.equals(null) ||
            listaPlatillos.get(position).descripcionPlatillo.equals("null")
        ){
            etDescripcionPlatillo.setText("")
        } else {
            etDescripcionPlatillo.setText(listaPlatillos.get(position).descripcionPlatillo)
        }

        var spiCategoria = vistaAlertDialog.findViewById<Spinner>(R.id.spiCategorias)
        var listaSpinner = MetodosVolley(context).obtenerListaSpinnerCategoria()
        spiCategoria.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, listaSpinner)

        spiCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                auxSpinnerPosition = position
                auxSpinnerText = parent.getItemAtPosition(position).toString()
            }
        }

        var etPrecio = vistaAlertDialog.findViewById<EditText>(R.id.etPrecio)
        etPrecio.setText(listaPlatillos.get(position).precio)

        builder.setView(vistaAlertDialog)
            // Add action buttons
            .setPositiveButton("ACEPTAR",
                DialogInterface.OnClickListener { dialog, id ->
                    editarPlatillo(
                        listaPlatillos.get(position).nombrePlatillo,
                        etNomPlatillo.text.toString(),
                        etDescripcionPlatillo.text.toString(),
                        etPrecio.text.toString(),
                        position)
                })
            .setNegativeButton("CANCELAR",
                DialogInterface.OnClickListener { dialog, id ->

                })
        builder.create()
        builder.setCancelable(false)
        builder.show()
    }

    fun editarPlatillo(
        oldNomPlatillo: String,
        etNomPlatillo: String,
        etDescripcion: String,
        etPrecio: String,
        position: Int

    ){
        // En este apartado se programa lo que deseamos hacer en caso de no haber errores
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, context.resources.getString(R.string.URL_API),
            Response.Listener { response ->

                Toast.makeText(context, response, Toast.LENGTH_LONG).show()

                try {

                    var jsonObject = JSONObject(response)
                    if(jsonObject.getString("codigo").equals("OK")) {
                        listaPlatillos.get(position).nombrePlatillo = etNomPlatillo
                        listaPlatillos.get(position).descripcionPlatillo = etDescripcion
                        listaPlatillos.get(position).categoria = auxSpinnerText
                        listaPlatillos.get(position).precio = etPrecio

                        notifyDataSetChanged()
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
                parametros["accion"] = "305"
                parametros["oldNomPlatillo"] = oldNomPlatillo
                parametros["nomPlatillo"] = etNomPlatillo
                parametros["descPlatillo"] = etDescripcion
                parametros["categoria"] = auxSpinnerText
                parametros["precio"] = etPrecio

                return parametros
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    fun borrarPlatillo(listaPlatillos: ArrayList<Platillo>, position:Int) {
        // En este apartado se programa lo que deseamos hacer en caso de no haber errores
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, context.resources.getString(R.string.URL_API),
            Response.Listener { response ->

                try {

                    var jsonObject = JSONObject(response)
                    if(jsonObject.getString("codigo").equals("OK")) {
                        Toast.makeText(context, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show()
                        listaPlatillos.removeAt(position)
                        notifyDataSetChanged()
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
                parametros["accion"] = "304"
                parametros["nomPlatillo"] = listaPlatillos.get(position).nombrePlatillo
                return parametros
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    override fun getItemCount(): Int {
        return listaPlatillos.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val tvNombrePlatillo = itemView.findViewById<TextView>(R.id.tvNombrePlatillo)
        val tvCategoria = itemView.findViewById<TextView>(R.id.tvCategoriaPlatillo)
        val tvDescripcion = itemView.findViewById<TextView>(R.id.tvDescripcionPlatillo)
        val precio = itemView.findViewById<TextView>(R.id.tvPrecio)
        val ivEditarPlatillo = itemView.findViewById<ImageView>(R.id.ivEditarPlatillo)
        val ivBorrarPlatillo = itemView.findViewById<ImageView>(R.id.ivBorrarPlatillo)

    }
}