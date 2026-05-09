package com.example.actividad2.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.actividad2.R
import com.example.actividad2.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class Registro : AppCompatActivity() {

private lateinit var etNombres: EditText
private lateinit var etApellidos: EditText
private lateinit var etCorreo: EditText
private lateinit var etContra: EditText
private lateinit var etRepetirContra: EditText
private lateinit var chkTerminos: CheckBox
private lateinit var btnRegistrate: Button
private lateinit var tvLogin: TextView

@Serializable
data class UsuarioData(
    val id: String,
    val nombres: String,
    val apellidos: String,
    val correo: String
)

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(R.layout.activity_registro)

    val rootView = findViewById<ViewGroup>(R.id.main)

    ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
        val bottomPadding = maxOf(systemBars.bottom, imeInsets.bottom)

        v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding)
        insets
    }



    // Inicializar vistas
    etNombres = findViewById(R.id.editNombres)
    etApellidos = findViewById(R.id.editApellidos)
    etCorreo = findViewById(R.id.editCorreo)
    etContra = findViewById(R.id.editPassword)
    etRepetirContra = findViewById(R.id.editRepetirPassword)
    chkTerminos = findViewById(R.id.checkTerminos)
    btnRegistrate = findViewById(R.id.btnRegistro)
    tvLogin = findViewById(R.id.textIniciarSesion)

    // CLICK BOTÓN REGISTRO
    btnRegistrate.setOnClickListener {

        val nombres = etNombres.text.toString().trim()
        val apellidos = etApellidos.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val contra = etContra.text.toString().trim()
        val repetirContra = etRepetirContra.text.toString().trim()

        // Validaciones básicas
        if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || contra.isEmpty() || repetirContra.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        if (contra.length < 8) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        if (contra != repetirContra) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        if (!chkTerminos.isChecked) {
            Toast.makeText(this, "Por favor, acepte los términos y condiciones", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }


        // REGISTRO EN SUPABASE
        lifecycleScope.launch {
            try {
                // Paso 1: Registrar usuario en Auth
                SupabaseClient.client.auth.signUpWith(Email) {
                    email = correo
                    password = contra
                }

                // Paso 2: Obtener el ID del usuario recién creado
                val userId = SupabaseClient.client.auth.currentUserOrNull()?.id
                    ?: throw Exception("No se pudo obtener el ID del usuario")

                // Paso 3: Guardar datos adicionales en la tabla 'usuarios'
                SupabaseClient.client.postgrest.from("usuarios").insert(
                    UsuarioData(
                        id = userId,
                        nombres = nombres,
                        apellidos = apellidos,
                        correo = correo
                    )
                )

                // Paso 4: Notificar y redirigir
                runOnUiThread {
                    Toast.makeText(this@Registro, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Registro, Login::class.java))
                    finish()
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@Registro, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
                e.printStackTrace()
            }
        }
    }

    // CLICK IR A LOGIN
    tvLogin.setOnClickListener {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}
}

