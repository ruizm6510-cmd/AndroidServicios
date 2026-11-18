package com.example.androidservicios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnEjercicio1).setOnClickListener {
            startActivity(Intent(this, Ejercicio1Activity::class.java))
        }

        findViewById<Button>(R.id.btnEjercicio2).setOnClickListener {
            startActivity(Intent(this, Ejercicio2Activity::class.java))
        }

        findViewById<Button>(R.id.btnEjercicio3).setOnClickListener {
            startActivity(Intent(this, Ejercicio3Activity::class.java))
        }

        findViewById<Button>(R.id.btnEjercicio4).setOnClickListener {
            startActivity(Intent(this, Ejercicio4Activity::class.java))
        }

        findViewById<Button>(R.id.btnEjercicio5).setOnClickListener {
            startActivity(Intent(this, Ejercicio5Activity::class.java))
        }

        findViewById<Button>(R.id.btnEjercicio6).setOnClickListener {
            startActivity(Intent(this, Ejercicio6Activity::class.java))
        }

        findViewById<Button>(R.id.btnEjercicio7).setOnClickListener {
            startActivity(Intent(this, Ejercicio7Activity::class.java))
        }
    }
}
