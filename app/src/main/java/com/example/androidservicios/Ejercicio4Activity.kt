package com.example.androidservicios

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest

class Ejercicio4Activity : AppCompatActivity() {

    private lateinit var btnEliminarUsuario: Button
    private lateinit var tvResultadoDelete: TextView

    private val urlApi = "https://jsonplaceholder.typicode.com/posts/1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio4)

        btnEliminarUsuario = findViewById(R.id.btnEliminarUsuario)
        tvResultadoDelete = findViewById(R.id.tvResultadoDelete)

        btnEliminarUsuario.setOnClickListener {
            eliminarUsuario()
        }
    }

    private fun eliminarUsuario() {
        tvResultadoDelete.text = "Enviando petición DELETE a $urlApi..."

        val peticion = object : StringRequest(
            Method.DELETE,
            urlApi,
            { respuesta ->
                val resultadoText = StringBuilder()
                resultadoText.append("SUCESO: Operación DELETE completada!\n\n")
                resultadoText.append(">>> Usuario eliminado <<<\n\n")
                resultadoText.append("Código HTTP de Respuesta: 200 OK\n")
                resultadoText.append("Respuesta del Servidor:\n$respuesta")

                tvResultadoDelete.text = resultadoText.toString()
                Toast.makeText(this, "Usuario eliminado con éxito", Toast.LENGTH_SHORT).show()
            },
            { error ->
                val errorMsg = "Error en DELETE: ${error.message ?: error.toString()}"
                tvResultadoDelete.text = errorMsg
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun parseNetworkResponse(response: com.android.volley.NetworkResponse?): com.android.volley.Response<String> {
                val statusCode = response?.statusCode ?: 0
                val result = super.parseNetworkResponse(response)
                return result
            }
        }

        VolleySingleton.getInstance(this).addToRequestQueue(peticion)
    }
}
