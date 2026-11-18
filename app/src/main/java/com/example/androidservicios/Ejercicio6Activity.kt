package com.example.androidservicios

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class Ejercicio6Activity : AppCompatActivity() {

    private lateinit var rvPersonajes: RecyclerView
    private lateinit var progressBar6: ProgressBar
    private val characterList = mutableListOf<Character>()
    private lateinit var adapter: CharacterAdapter

    private val urlApi = "https://rickandmortyapi.com/api/character"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio6)

        rvPersonajes = findViewById(R.id.rvPersonajes)
        progressBar6 = findViewById(R.id.progressBar6)

        rvPersonajes.layoutManager = GridLayoutManager(this, 2)
        adapter = CharacterAdapter(characterList) { character ->
            // Al hacer clic en un personaje, abrir DetailActivity
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("CHARACTER", character)
            }
            startActivity(intent)
        }
        rvPersonajes.adapter = adapter

        cargarGaleria()
    }

    private fun cargarGaleria() {
        progressBar6.visibility = View.VISIBLE

        val peticion = JsonObjectRequest(
            Request.Method.GET,
            urlApi,
            null,
            { respuesta ->
                progressBar6.visibility = View.GONE
                try {
                    val results = respuesta.getJSONArray("results")
                    characterList.clear()

                    for (i in 0 until results.length()) {
                        val obj = results.getJSONObject(i)
                        val id = obj.getInt("id")
                        val name = obj.getString("name")
                        val species = obj.getString("species")
                        val status = obj.getString("status")
                        val origin = obj.getJSONObject("origin").getString("name")
                        val imageUrl = obj.getString("image")

                        characterList.add(Character(id, name, species, status, origin, imageUrl))
                    }

                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al procesar JSON: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                progressBar6.visibility = View.GONE
                Toast.makeText(this, "Error al obtener galería: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(peticion)
    }
}
