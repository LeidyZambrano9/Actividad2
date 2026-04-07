package com.example.actividad2.ui.main

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.actividad2.R
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

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: androidx.drawerlayout.widget.DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        cargarFragment(fragment = HomeFragment())
        bottomNav.selectedItemId = R.id.nav_home

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
                R.id.nav_info_drawer -> cargarFragment(fragment = InfoFragment())
            }
            drawerLayout.closeDrawers()
            true
        }

    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}