package com.example.androidservicios

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.ImageRequest

class DetailActivity : AppCompatActivity() {

    private lateinit var ivDetailImagen: ImageView
    private lateinit var tvDetailNombre: TextView
    private lateinit var tvDetailEspecie: TextView
    private lateinit var tvDetailEstado: TextView
    private lateinit var tvDetailOrigen: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        ivDetailImagen = findViewById(R.id.ivDetailImagen)
        tvDetailNombre = findViewById(R.id.tvDetailNombre)
        tvDetailEspecie = findViewById(R.id.tvDetailEspecie)
        tvDetailEstado = findViewById(R.id.tvDetailEstado)
        tvDetailOrigen = findViewById(R.id.tvDetailOrigen)

        val character = intent.getSerializableExtra("CHARACTER") as? Character
        if (character != null) {
            tvDetailNombre.text = character.name
            tvDetailEspecie.text = "Especie: ${character.species}"
            tvDetailEstado.text = "Estado: ${character.status}"
            tvDetailOrigen.text = "Origen: ${character.origin}"

            // Cargar la imagen en grande usando Volley ImageRequest
            val imageRequest = ImageRequest(
                character.imageUrl,
                { bitmap: Bitmap ->
                    ivDetailImagen.setImageBitmap(bitmap)
                },
                0,
                0,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.ARGB_8888,
                { error ->
                    Toast.makeText(this, "Error al cargar imagen en alta resolución: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )

            VolleySingleton.getInstance(this).addToRequestQueue(imageRequest)
        } else {
            Toast.makeText(this, "No se recibieron datos del personaje", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
