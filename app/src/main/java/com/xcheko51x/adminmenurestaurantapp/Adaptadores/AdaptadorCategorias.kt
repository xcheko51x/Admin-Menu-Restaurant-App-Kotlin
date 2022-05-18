package com.xcheko51x.adminmenurestaurantapp.Adaptadores

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.xcheko51x.adminmenurestaurantapp.Clases.Categoria
import com.xcheko51x.adminmenurestaurantapp.MetodosVolley
import com.xcheko51x.adminmenurestaurantapp.R
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class AdaptadorCategorias(
    val context: Context,
    var listaCategorias: ArrayList<Categoria>
): RecyclerView.Adapter<AdaptadorCategorias.ViewHolder>()
{
    var rutaImagenes = context.resources.getString(R.string.RUTA_IMAGENES)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_categorias, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoria = listaCategorias[position]

        println("${rutaImagenes}${categoria.imagenCategoria}")
        Glide.with(context)
            .load("${rutaImagenes}${categoria.imagenCategoria}")
            .centerInside()
            .placeholder(R.drawable.icon_falta_foto)
            .centerInside()
            .into(holder.ivCategoria)
        holder.tvNomCategoria.text = categoria.nombreCategoria!!.uppercase()

        holder.ibtnEdit.setOnClickListener {
            formEditarCategoria(position)
        }

        holder.ibtnBorrar.setOnClickListener {
            borrarCategoria(listaCategorias, position)
        }
    }

    override fun getItemCount(): Int {
        return listaCategorias.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivCategoria = itemView.findViewById<ImageView>(R.id.ivCategoria)
        val ibtnEdit = itemView.findViewById<ImageButton>(R.id.ibtnEdit)
        val ibtnBorrar = itemView.findViewById<ImageButton>(R.id.ibtnBorrar)
        val tvNomCategoria = itemView.findViewById<TextView>(R.id.tvNomCategoria)
    }

    fun formEditarCategoria(position: Int) {
        val builder = AlertDialog.Builder(context)

        var vistaAlertDialog = LayoutInflater.from(context).inflate(R.layout.alert_dialog_add_edit_categoria, null)

        var tvAccion = vistaAlertDialog.findViewById<TextView>(R.id.tvTitulo)
        tvAccion.text = "EDITAR CATEGORIA"

        var etNomCategoria = vistaAlertDialog.findViewById<EditText>(R.id.etNomCategoria)
        etNomCategoria.setText(listaCategorias.get(position).nombreCategoria)
        builder.setView(vistaAlertDialog)
            // Add action buttons
            .setPositiveButton("ACEPTAR",
                DialogInterface.OnClickListener { dialog, id ->
                    editarCategoria(listaCategorias.get(position).nombreCategoria.toString(), etNomCategoria.text.toString(), position)
                })
            .setNegativeButton("CANCELAR",
                DialogInterface.OnClickListener { dialog, id ->

                })
        builder.create()
        builder.setCancelable(false)
        builder.show()
    }

    fun editarCategoria(oldNomCategoria: String, nomCategoria: String, position: Int) {
        // En este apartado se programa lo que deseamos hacer en caso de no haber errores
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, context.resources.getString(R.string.URL_API),
            Response.Listener { response ->

                try {

                    var jsonObject = JSONObject(response)
                    if(jsonObject.getString("codigo").equals("OK")) {
                        listaCategorias.get(position).nombreCategoria = nomCategoria.lowercase()
                        listaCategorias.get(position).imagenCategoria = "icon_${nomCategoria.lowercase()}.png"
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
                parametros["accion"] = "204"
                parametros["nomCategoria"] = nomCategoria.lowercase()
                parametros["oldNomCategoria"] = oldNomCategoria.lowercase()
                return parametros
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    fun borrarCategoria(listaCategorias: ArrayList<Categoria>, position:Int) {

        // En este apartado se programa lo que deseamos hacer en caso de no haber errores
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, context.resources.getString(R.string.URL_API),
            Response.Listener { response ->

                try {

                    var jsonObject = JSONObject(response)
                    if(jsonObject.getString("codigo").equals("OK")) {
                        Toast.makeText(context, jsonObject.getString("mensaje"), Toast.LENGTH_LONG).show()
                        listaCategorias.removeAt(position)
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
                parametros["accion"] = "202"
                parametros["categoria"] = listaCategorias.get(position).nombreCategoria.toString()
                return parametros
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}