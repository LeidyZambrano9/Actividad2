package com.example.actividad2.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.example.actividad2.R
import com.example.actividad2.SupabaseClient
import com.example.actividad2.data.CredentialesManager
import com.example.actividad2.ui.main.MainActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class Login : AppCompatActivity() {

    private lateinit var etCorreo: EditText
    private lateinit var etContra: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoogle: ImageButton
    private lateinit var tvRegistrate: TextView
    private lateinit var tvRecuperar: TextView
    private lateinit var tvHuellaDigital: TextView

    @Serializable
    data class UsuarioData(
        val nombres: String,
        val apellidos: String,
        val correo: String,
        val contrasena: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Inicializar vistas
        etCorreo = findViewById(R.id.editCorreo)
        etContra = findViewById(R.id.editPassword)
        btnLogin = findViewById(R.id.Ingresar_login)
        btnGoogle = findViewById(R.id.imageButton2)
        tvRegistrate = findViewById(R.id.Registro_Login)
        tvRecuperar = findViewById(R.id.Recupera_Login)
        tvHuellaDigital = findViewById(R.id.Ingresa_Huella)

        val rootView = findViewById<ViewGroup>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = maxOf(systemBars.bottom, imeInsets.bottom)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding)
            insets
        }

        if (CredentialesManager.huellaActiva(this)) {
            tvHuellaDigital.visibility = View.VISIBLE
        } else {
            tvHuellaDigital.visibility = View.GONE
        }

        btnLogin.setOnClickListener {
            iniciarsesion()
        }

        tvRegistrate.setOnClickListener {
            startActivity(Intent(this, Registro::class.java))
        }

        tvRecuperar.setOnClickListener {
            Toast.makeText(this, "Funcionalidad próximamente", Toast.LENGTH_SHORT).show()
        }

        tvHuellaDigital.setOnClickListener {
            mostrarDialogHuella()
        }

        // Configurar botón de Google
        btnGoogle.setOnClickListener {
            iniciarSesionConGoogle()
        }
    }

    private fun iniciarsesion() {
        val correo = etCorreo.text.toString().trim()
        val contra = etContra.text.toString().trim()

        if (correo.isEmpty() || contra.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val usuario = SupabaseClient.client.from("usuarios")
                    .select {
                        filter {
                            eq("correo", correo)
                            eq("contrasena", contra)
                        }
                    }.decodeSingleOrNull<UsuarioData>()

                if (usuario != null) {
                    CredentialesManager.guardarCredenciales(this@Login, correo, contra, true)
                    irAPantallaPrincipal()
                } else {
                    Toast.makeText(this@Login, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@Login, "Error al iniciar sesión: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun iniciarSesionConGoogle() {
        lifecycleScope.launch {
            try {
                // 1. Configurar la solicitud de Google con tu Client ID
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("415075410923-es3rbeb4ll2cadcirqpi0rikj2q3r547.apps.googleusercontent.com")
                    .setAutoSelectEnabled(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // 2. Mostrar el selector de cuentas
                val credentialManager = CredentialManager.create(this@Login)
                val result = credentialManager.getCredential(this@Login, request)

                // 3. Extraer el ID Token
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)

                // 4. Autenticar en Supabase usando el token de Google
                SupabaseClient.client.auth.signInWith(IDToken) {
                    idToken = googleIdTokenCredential.idToken
                    provider = Google
                }
                
                irAPantallaPrincipal()
            } catch (e: Exception) {
                Toast.makeText(this@Login, "Error Google: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun mostrarDialogHuella() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                val correo = CredentialesManager.obtenerCorreo(this@Login)
                val contra = CredentialesManager.obtenerContrasena(this@Login)

                if (correo != null && contra != null) {
                    lifecycleScope.launch {
                        try {
                            val usuario = SupabaseClient.client.from("usuarios")
                                .select {
                                    filter {
                                        eq("correo", correo)
                                        eq("contrasena", contra)
                                    }
                                }.decodeSingleOrNull<UsuarioData>()

                            if (usuario != null) {
                                irAPantallaPrincipal()
                            } else {
                                Toast.makeText(this@Login, "Credenciales guardadas ya no son válidas", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this@Login, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Acceso con huella")
            .setSubtitle("Usa tu huella para ingresar")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun irAPantallaPrincipal() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }
}
