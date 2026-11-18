package com.example.androidservicios

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class Ejercicio3Activity : AppCompatActivity() {

    private lateinit var etTitulo: EditText
    private lateinit var etCuerpo: EditText
    private lateinit var btnPut: Button
    private lateinit var btnPatch: Button
    private lateinit var tvResultadoPutPatch: TextView

    private val urlApi = "https://jsonplaceholder.typicode.com/posts/1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio3)

        etTitulo = findViewById(R.id.etTitulo)
        etCuerpo = findViewById(R.id.etCuerpo)
        btnPut = findViewById(R.id.btnPut)
        btnPatch = findViewById(R.id.btnPatch)
        tvResultadoPutPatch = findViewById(R.id.tvResultadoPutPatch)

        btnPut.setOnClickListener {
            ejecutarPut()
        }

        btnPatch.setOnClickListener {
            ejecutarPatch()
        }
    }

    private fun ejecutarPut() {
        val titulo = etTitulo.text.toString().trim()
        val cuerpo = etCuerpo.text.toString().trim()

        tvResultadoPutPatch.text = "Enviando petición PUT (reemplazo completo) a $urlApi..."

        // PUT: Reemplaza todo el objeto (título, cuerpo, userId)
        val jsonParams = JSONObject().apply {
            put("id", 1)
            put("title", titulo)
            put("body", cuerpo)
            put("userId", 1)
        }

        val peticion = JsonObjectRequest(
            Request.Method.PUT,
            urlApi,
            jsonParams,
            { respuesta ->
                val text = "REEMPLAZADO VÍA PUT:\n\n" + respuesta.toString(2)
                tvResultadoPutPatch.text = text
                Toast.makeText(this, "PUT completado exitosamente", Toast.LENGTH_SHORT).show()
            },
            { error ->
                val errorMsg = "Error en PUT: ${error.message ?: error.toString()}"
                tvResultadoPutPatch.text = errorMsg
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(peticion)
    }

    private fun ejecutarPatch() {
        val titulo = etTitulo.text.toString().trim()

        tvResultadoPutPatch.text = "Enviando petición PATCH (modificación parcial del título) a $urlApi..."

        // PATCH: Solo modifica el campo 'title'
        val jsonParams = JSONObject().apply {
            put("title", titulo)
        }

        val peticion = JsonObjectRequest(
            Request.Method.PATCH,
            urlApi,
            jsonParams,
            { respuesta ->
                val text = "MODIFICADO VÍA PATCH (Solo Título):\n\n" + respuesta.toString(2)
                tvResultadoPutPatch.text = text
                Toast.makeText(this, "PATCH completado exitosamente", Toast.LENGTH_SHORT).show()
            },
            { error ->
                val errorMsg = "Error en PATCH: ${error.message ?: error.toString()}"
                tvResultadoPutPatch.text = errorMsg
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(peticion)
    }
}
