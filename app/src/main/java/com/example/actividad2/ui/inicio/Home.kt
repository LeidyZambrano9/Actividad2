package com.example.actividad2.ui.inicio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.actividad2.R
import com.example.actividad2.ui.auth.Login
import com.example.actividad2.ui.auth.Registro

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val boton = findViewById<Button>(R.id.button_home)
        boton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }


        val textoRegistrate = findViewById<TextView>(R.id.Registrate_home)

        textoRegistrate.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }
    }
}