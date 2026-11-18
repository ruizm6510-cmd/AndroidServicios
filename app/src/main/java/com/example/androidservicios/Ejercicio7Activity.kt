package com.example.androidservicios

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONArray
import org.json.JSONObject

class Ejercicio7Activity : AppCompatActivity() {

    private lateinit var etUrlServer: EditText
    private lateinit var etBookId: EditText
    private lateinit var etBookTitulo: EditText
    private lateinit var etBookAutor: EditText

    private lateinit var btnGetBook: Button
    private lateinit var btnPostBook: Button
    private lateinit var btnPutBook: Button
    private lateinit var btnPatchBook: Button
    private lateinit var btnDeleteBook: Button

    private lateinit var rvLibros: RecyclerView
    private lateinit var tvLogEjercicio7: TextView

    private val bookList = mutableListOf<Book>()
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio7)

        etUrlServer = findViewById(R.id.etUrlServer)
        etBookId = findViewById(R.id.etBookId)
        etBookTitulo = findViewById(R.id.etBookTitulo)
        etBookAutor = findViewById(R.id.etBookAutor)

        btnGetBook = findViewById(R.id.btnGetBook)
        btnPostBook = findViewById(R.id.btnPostBook)
        btnPutBook = findViewById(R.id.btnPutBook)
        btnPatchBook = findViewById(R.id.btnPatchBook)
        btnDeleteBook = findViewById(R.id.btnDeleteBook)

        rvLibros = findViewById(R.id.rvLibros)
        tvLogEjercicio7 = findViewById(R.id.tvLogEjercicio7)

        rvLibros.layoutManager = LinearLayoutManager(this)
        adapter = BookAdapter(bookList) { selectedBook ->
            etBookId.setText(selectedBook.id.toString())
            etBookTitulo.setText(selectedBook.titulo)
            etBookAutor.setText(selectedBook.autor)
            log("Seleccionado libro ID ${selectedBook.id}")
        }
        rvLibros.adapter = adapter

        btnGetBook.setOnClickListener { obtenerLibros() }
        btnPostBook.setOnClickListener { crearLibro() }
        btnPutBook.setOnClickListener { actualizarLibroPut() }
        btnPatchBook.setOnClickListener { actualizarLibroPatch() }
        btnDeleteBook.setOnClickListener { eliminarLibro() }
    }

    private fun getUrl(): String {
        return etUrlServer.text.toString().trim()
    }

    private fun log(msg: String) {
        tvLogEjercicio7.text = msg
    }

    // GET → Mostrar libros
    private fun obtenerLibros() {
        val url = getUrl()
        log("Ejecutando GET a $url...")

        val peticion = StringRequest(
            Request.Method.GET,
            url,
            { respuesta ->
                log("GET Exitoso:\n$respuesta")
                try {
                    bookList.clear()
                    // Probar si viene como un JSONArray o JSONObj
                    if (respuesta.trim().startsWith("[")) {
                        val jsonArray = JSONArray(respuesta)
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val id = obj.optInt("id", i + 1)
                            val titulo = obj.optString("titulo", obj.optString("title", "Sin Título"))
                            val autor = obj.optString("autor", obj.optString("body", "Sin Autor"))
                            bookList.add(Book(id, titulo, autor))
                        }
                    } else if (respuesta.trim().startsWith("{")) {
                        val obj = JSONObject(respuesta)
                        val id = obj.optInt("id", 1)
                        val titulo = obj.optString("titulo", obj.optString("title", "Sin Título"))
                        val autor = obj.optString("autor", obj.optString("body", "Sin Autor"))
                        bookList.add(Book(id, titulo, autor))
                    }
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "Se cargaron ${bookList.size} libros", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    log("Error al parsear libros: ${e.message}\nRespuesta cruda:\n$respuesta")
                }
            },
            { error ->
                log("Error en GET: ${error.message ?: error.toString()}\n" +
                        "Asegúrese de ejecutar json-server: npx json-server --watch db.json --port 3000")
                Toast.makeText(this, "Error en GET. Verifique conexión a $url", Toast.LENGTH_LONG).show()
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(peticion)
    }

    // POST → Agregar libro
    private fun crearLibro() {
        val url = getUrl()
        val titulo = etBookTitulo.text.toString().trim()
        val autor = etBookAutor.text.toString().trim()

        if (titulo.isEmpty()) {
            Toast.makeText(this, "Ingrese el título del libro", Toast.LENGTH_SHORT).show()
            return
        }

        log("Ejecutando POST a $url...")

        val datos = JSONObject().apply {
            put("titulo", titulo)
            put("autor", autor)
        }

        val peticion = JsonObjectRequest(
            Request.Method.POST,
            url,
            datos,
            { respuesta ->
                log("POST Exitoso! Libro creado:\n${respuesta.toString(2)}")
                Toast.makeText(this, "Libro agregado correctamente", Toast.LENGTH_SHORT).show()
                obtenerLibros() // Recargar lista
            },
            { error ->
                log("Error en POST: ${error.message ?: error.toString()}")
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(peticion)
    }

    // PUT → Actualizar libro
    private fun actualizarLibroPut() {
        val idStr = etBookId.text.toString().trim()
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID del libro para PUT", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "${getUrl()}/$idStr"
        val titulo = etBookTitulo.text.toString().trim()
        val autor = etBookAutor.text.toString().trim()

        log("Ejecutando PUT a $url...")

        val datos = JSONObject().apply {
            put("id", idStr.toInt())
            put("titulo", titulo)
            put("autor", autor)
        }

        val peticion = JsonObjectRequest(
            Request.Method.PUT,
            url,
            datos,
            { respuesta ->
                log("PUT Exitoso! Libro actualizado (reemplazo completo):\n${respuesta.toString(2)}")
                Toast.makeText(this, "Libro ID $idStr actualizado", Toast.LENGTH_SHORT).show()
                obtenerLibros()
            },
            { error ->
                log("Error en PUT: ${error.message ?: error.toString()}")
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(peticion)
    }

    // PATCH → Modificar título del libro
    private fun actualizarLibroPatch() {
        val idStr = etBookId.text.toString().trim()
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID del libro para PATCH", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "${getUrl()}/$idStr"
        val titulo = etBookTitulo.text.toString().trim()

        log("Ejecutando PATCH a $url...")

        val datos = JSONObject().apply {
            put("titulo", titulo)
        }

        val peticion = JsonObjectRequest(
            Request.Method.PATCH,
            url,
            datos,
            { respuesta ->
                log("PATCH Exitoso! Título modificado:\n${respuesta.toString(2)}")
                Toast.makeText(this, "Título de Libro ID $idStr modificado", Toast.LENGTH_SHORT).show()
                obtenerLibros()
            },
            { error ->
                log("Error en PATCH: ${error.message ?: error.toString()}")
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(peticion)
    }

    // DELETE → Eliminar libro
    private fun eliminarLibro() {
        val idStr = etBookId.text.toString().trim()
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID del libro a eliminar", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "${getUrl()}/$idStr"
        log("Ejecutando DELETE a $url...")

        val peticion = StringRequest(
            Request.Method.DELETE,
            url,
            { respuesta ->
                log("DELETE Exitoso! Libro ID $idStr eliminado.\nRespuesta:\n$respuesta")
                Toast.makeText(this, "Libro ID $idStr eliminado", Toast.LENGTH_SHORT).show()
                obtenerLibros()
            },
            { error ->
                log("Error en DELETE: ${error.message ?: error.toString()}")
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(peticion)
    }
}
