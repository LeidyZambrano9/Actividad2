package com.example.actividad2.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.actividad2.R
import com.example.actividad2.ui.main.MainActivity

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- Lógica para el botón INGRESAR ---
        val botonIngresar = findViewById<Button>(R.id.Ingresar_login)
        botonIngresar.setOnClickListener {
            // Creamos el salto a MainActivity
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
        }

        // --- Tu lógica de Registro existente ---
        val textoRegistro = findViewById<TextView>(R.id.Registro_Login)
        textoRegistro.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }
    }
}