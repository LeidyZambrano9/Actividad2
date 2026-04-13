package com.example.actividad2.ui.auth

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.actividad2.R
import com.example.actividad2.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class Registro : AppCompatActivity() {

    private lateinit var etNombres: EditText
    private lateinit var etApellidos: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText
    private lateinit var etReContrasena: EditText
    private lateinit var checkTerminos: CheckBox
    private lateinit var btnRegistro: Button
    private lateinit var tvCuenta: TextView

    private lateinit var textIniciarSesion: TextView
    private lateinit var textTerminos: TextView


    @Serializable
    data class UsuarioData(
        val nombres: String,
        val apellidos: String,
        val correo: String,
        val contrasena: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        etNombres = findViewById<EditText>(R.id.editNombres)
        etApellidos = findViewById<EditText>(R.id.editApellidos)
        etCorreo = findViewById<EditText>(R.id.editCorreo)
        etContrasena = findViewById<EditText>(R.id.editPassword)
        etReContrasena = findViewById<EditText>(R.id.editRepetirPassword)
        checkTerminos = findViewById<CheckBox>(R.id.checkTerminos)
        btnRegistro = findViewById<Button>(R.id.btnRegistro)
        tvCuenta = findViewById<TextView>(R.id.textYaTienesCuenta)

        textIniciarSesion = findViewById<TextView>(R.id.textIniciarSesion)
        textTerminos = findViewById<TextView>(R.id.textTerminos)


        // Volver al Login
        textIniciarSesion.setOnClickListener {
            finish() // Vuelve a la actividad anterior (Login)
        }

        // Click en Términos y Condiciones
        textTerminos.setOnClickListener {
            Toast.makeText(this, "Abrir términos y condiciones", Toast.LENGTH_SHORT).show()
        }

        //Escuchar el boton de registro
        btnRegistro.setOnClickListener {

            val nombres = etNombres.text.toString().trim()
            val apellidos = etApellidos.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()
            val reContrasena = etReContrasena.text.toString().trim()

            // Validaciones básicas
            if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contrasena != reContrasena) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!checkTerminos.isChecked) {
                Toast.makeText(this, "Debes aceptar los términos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuario = UsuarioData(
                nombres = nombres,
                apellidos = apellidos,
                correo = correo,
                contrasena = contrasena
            )

            // Insertar en Supabase
            lifecycleScope.launch {
                try {
                    SupabaseClient.client
                        .from("usuarios")
                        .insert(usuario)

                    Toast.makeText(this@Registro, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()

                } catch (e: Exception) {
                    Toast.makeText(this@Registro, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
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