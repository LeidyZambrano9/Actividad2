package com.example.actividad2.Activities

import com.example.actividad2.R
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView // No olvides importar TextView
import androidx.appcompat.app.AppCompatActivity

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