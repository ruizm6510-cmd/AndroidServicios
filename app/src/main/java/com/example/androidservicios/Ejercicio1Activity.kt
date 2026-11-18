package com.example.androidservicios

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import org.json.JSONArray
import org.json.JSONException

class Ejercicio1Activity : AppCompatActivity() {

    private lateinit var btnCargarUsuarios: Button
    private lateinit var tvResultadoNombres: TextView
    private lateinit var tvResultadoJson: TextView

    private val urlApi = "https://jsonplaceholder.typicode.com/users"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio1)

        btnCargarUsuarios = findViewById(R.id.btnCargarUsuarios)
        tvResultadoNombres = findViewById(R.id.tvResultadoNombres)
        tvResultadoJson = findViewById(R.id.tvResultadoJson)

        btnCargarUsuarios.setOnClickListener {
            obtenerUsuarios()
        }
    }

    private fun obtenerUsuarios() {
        tvResultadoNombres.text = "Cargando usuarios..."
        tvResultadoJson.text = "Petición enviada a $urlApi..."

        val peticion = StringRequest(
            Request.Method.GET,
            urlApi,
            { respuestaJson ->
                // Mostrar JSON crudo en pantalla
                tvResultadoJson.text = respuestaJson

                // Parsear respuesta JSON y mostrar solo los nombres de los usuarios, uno por línea
                try {
                    val jsonArray = JSONArray(respuestaJson)
                    val builderNombres = StringBuilder()
                    builderNombres.append("Nombres de Usuarios:\n\n")

                    for (i in 0 until jsonArray.length()) {
                        val usuarioObj = jsonArray.getJSONObject(i)
                        val nombre = usuarioObj.getString("name")
                        builderNombres.append("${i + 1}. $nombre\n")
                    }

                    tvResultadoNombres.text = builderNombres.toString()
                } catch (e: JSONException) {
                    tvResultadoNombres.text = "Error al parsear JSON: ${e.message}"
                }
            },
            { error ->
                val errorMsg = "Error en la petición: ${error.message ?: error.toString()}"
                tvResultadoNombres.text = errorMsg
                tvResultadoJson.text = errorMsg
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(peticion)
    }
}
