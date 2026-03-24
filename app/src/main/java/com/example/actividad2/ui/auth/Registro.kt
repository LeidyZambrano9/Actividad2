package com.example.actividad2.ui.auth

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.actividad2.R

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val btnRegistro = findViewById<Button>(R.id.btnRegistro)
        val textIniciarSesion = findViewById<TextView>(R.id.textIniciarSesion)
        val textTerminos = findViewById<TextView>(R.id.textTerminos)
        val checkTerminos = findViewById<CheckBox>(R.id.checkTerminos)

        // Volver al Login
        textIniciarSesion.setOnClickListener {
            finish() // Vuelve a la actividad anterior (Login)
        }

        // Click en Términos y Condiciones
        textTerminos.setOnClickListener {
            Toast.makeText(this, "Abrir términos y condiciones", Toast.LENGTH_SHORT).show()
        }

        // Botón Registrar
        btnRegistro.setOnClickListener {
            if (checkTerminos.isChecked) {
                // Aquí iría la lógica de registro
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                // Ir al Home o Login
            } else {
                Toast.makeText(this, "Debes aceptar los términos", Toast.LENGTH_SHORT).show()
            }
        }

        val rootView = findViewById<ViewGroup>(R.id.main)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->

            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())

            val bottomPadding = maxOf(systemBars.bottom, imeInsets.bottom)

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                bottomPadding
            )

            insets
        }
    }
}