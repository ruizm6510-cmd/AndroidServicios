package com.example.androidservicios

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import org.json.JSONObject

class Ejercicio2Activity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etCorreo: EditText
    private lateinit var btnEnviarPost: Button
    private lateinit var tvResultadoPost: TextView

    private val urlApi = "https://jsonplaceholder.typicode.com/posts"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio2)

        etNombre = findViewById(R.id.etNombre)
        etCorreo = findViewById(R.id.etCorreo)
        btnEnviarPost = findViewById(R.id.btnEnviarPost)
        tvResultadoPost = findViewById(R.id.tvResultadoPost)

        btnEnviarPost.setOnClickListener {
            enviarPost()
        }
    }

    private fun enviarPost() {
        val nombre = etNombre.text.toString().trim()
        val correo = etCorreo.text.toString().trim()

        if (nombre.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        tvResultadoPost.text = "Enviando petición POST a $urlApi..."

        // Construir JSON con nombre y correo
        val jsonParams = JSONObject()
        try {
            jsonParams.put("title", nombre)
            jsonParams.put("body", correo)
            jsonParams.put("userId", 1)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val peticion = JsonObjectRequest(
            Request.Method.POST,
            urlApi,
            jsonParams,
            { respuesta ->
                try {
                    val idCreado = respuesta.optString("id", "Desconocido")
                    val resultadoText = StringBuilder()
                    resultadoText.append("SUCESO: Petición POST Exitosa!\n\n")
                    resultadoText.append(">>> Usuario creado con ID: $idCreado <<<\n\n")
                    resultadoText.append("Respuesta del Servidor JSON:\n")
                    resultadoText.append(respuesta.toString(2))

                    tvResultadoPost.text = resultadoText.toString()

                    Toast.makeText(this, "Usuario creado con ID: $idCreado", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    tvResultadoPost.text = "Error al procesar la respuesta: ${e.message}"
                }
            },
            { error ->
                val errorMsg = "Error en POST: ${error.message ?: error.toString()}"
                tvResultadoPost.text = errorMsg
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(peticion)
    }
}
