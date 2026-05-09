package com.example.actividad2.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.actividad2.R
import com.example.actividad2.SupabaseClient
import com.example.actividad2.data.UsuarioRepository
import com.example.actividad2.ui.auth.Login
import com.example.actividad2.ui.main.admin.AdminFragment
import com.example.actividad2.ui.main.admin.UsuariosFragment
import com.example.actividad2.ui.main.perfil.PerfilFragment
import com.example.actividad2.ui.main.productos.CarritoFragment
import com.example.actividad2.ui.main.productos.CatalogoFragment
import com.example.actividad2.ui.main.productos.FavoritosFragment
import com.example.actividad2.ui.main.productos.HomeFragment
import com.example.actividad2.ui.main.productos.InfoFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import kotlin.jvm.java


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Carga el XML que construimos a partir del ID

        val toolbar = findViewById<Toolbar>(R.id.toolbar)//Paso 1 - La barra superior se conectara como action bar y mh
        drawerLayout = findViewById(R.id.drawer_layout) // contenedor raiz
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav) // menu inferior
        val navView = findViewById<NavigationView>(R.id.nav_view) // menu lateral

        setSupportActionBar(toolbar) //Le dice al activity que use nuestro tootlbar del XML como la barra de acción oficial

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle) // registra el toogle como listener del drawe
        toggle.syncState() // Sincroniza el estado del drawer
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.Color_app)

        cargarFragment(HomeFragment())
        bottomNav.selectedItemId = R.id.nav_home

        configurarMenuPorRol(navView.menu)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> cargarFragment(fragment = HomeFragment())
                R.id.nav_search -> cargarFragment(fragment = CatalogoFragment())
                R.id.nav_favorites -> cargarFragment(fragment = CarritoFragment())
                R.id.nav_profile -> cargarFragment(fragment = PerfilFragment())
            }
            true
        }

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_drawer -> cargarFragment(fragment = FavoritosFragment())
                R.id.nav_search_drawer -> cargarFragment(fragment = AdminFragment())
                R.id.nav_settings_drawer -> cargarFragment(fragment = UsuariosFragment())
                R.id.nav_info_drawer -> cerrarSesion()
            }
            drawerLayout.closeDrawers()
            true
        }

    }

    private fun configurarMenuPorRol(menu: Menu){
        lifecycleScope.launch {
            val rol = UsuarioRepository.obtenerRolActual()
            android.util.Log.d("DEBUG_ROL", "Rol obtenido: $rol")

            runOnUiThread {
                when (rol) {
                    "admin" ->{
                        //admin ve todo
                        menu.findItem(R.id.nav_admin).isVisible = true
                        menu.findItem(R.id.nav_usuarios).isVisible = true
                    }
                    "vendedor" ->{
                        //vendedor ve catalogo y carrito
                        menu.findItem(R.id.nav_admin).isVisible = true
                        menu.findItem(R.id.nav_usuarios).isVisible = false
                    }
                    else -> {
                        menu.findItem(R.id.nav_admin).isVisible = false
                        menu.findItem(R.id.nav_usuarios).isVisible = false
                    }
                }
            }

        }
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun cerrarSesion() {
        lifecycleScope.launch {
            try {
                SupabaseClient.client.auth.signOut()
                runOnUiThread {
                    startActivity(Intent(this@MainActivity, Login::class.java))
                    finishAffinity()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}