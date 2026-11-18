package com.example.androidservicios

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest

class Ejercicio5Activity : AppCompatActivity() {

    private lateinit var btnCargarPersonaje: Button
    private lateinit var ivPersonaje: ImageView
    private lateinit var tvNombrePersonaje: TextView
    private lateinit var tvEspeciePersonaje: TextView
    private lateinit var tvEstadoPersonaje: TextView

    private val urlApi = "https://rickandmortyapi.com/api/character"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio5)

        btnCargarPersonaje = findViewById(R.id.btnCargarPersonaje)
        ivPersonaje = findViewById(R.id.ivPersonaje)
        tvNombrePersonaje = findViewById(R.id.tvNombrePersonaje)
        tvEspeciePersonaje = findViewById(R.id.tvEspeciePersonaje)
        tvEstadoPersonaje = findViewById(R.id.tvEstadoPersonaje)

        btnCargarPersonaje.setOnClickListener {
            cargarPersonaje()
        }
    }

    private fun cargarPersonaje() {
        tvNombrePersonaje.text = "Cargando..."
        tvEspeciePersonaje.text = "Obteniendo datos de API..."
        tvEstadoPersonaje.text = ""

        val peticionJson = JsonObjectRequest(
            Request.Method.GET,
            urlApi,
            null,
            { respuesta ->
                try {
                    val results = respuesta.getJSONArray("results")
                    if (results.length() > 0) {
                        // Obtener el primer objeto personaje
                        val primerPersonaje = results.getJSONObject(0)
                        val nombre = primerPersonaje.getString("name")
                        val especie = primerPersonaje.getString("species")
                        val estado = primerPersonaje.getString("status")
                        val urlImagen = primerPersonaje.getString("image")

                        // Mostrar título/nombre y especie debajo del ImageView
                        tvNombrePersonaje.text = nombre
                        tvEspeciePersonaje.text = "Especie: $especie"
                        tvEstadoPersonaje.text = "Estado: $estado"

                        // Descargar y mostrar la imagen con Volley ImageRequest
                        cargarImagenVolley(urlImagen)
                    } else {
                        tvNombrePersonaje.text = "No se encontraron personajes"
                    }
                } catch (e: Exception) {
                    tvNombrePersonaje.text = "Error al procesar datos: ${e.message}"
                }
            },
            { error ->
                val errorMsg = "Error de red: ${error.message ?: error.toString()}"
                tvNombrePersonaje.text = errorMsg
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(peticionJson)
    }

    private fun cargarImagenVolley(urlImagen: String) {
        val solicitudImagen = ImageRequest(
            urlImagen,
            { bitmap: Bitmap ->
                ivPersonaje.setImageBitmap(bitmap)
            },
            0,
            0,
            ImageView.ScaleType.CENTER_CROP,
            Bitmap.Config.ARGB_8888,
            { error ->
                Toast.makeText(this, "Error al cargar la imagen: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(solicitudImagen)
    }
}
