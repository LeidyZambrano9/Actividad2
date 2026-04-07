package com.example.actividad2.ui.main.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.actividad2.R


class HomeFragment : Fragment() {
    private val listaProductos = listOf(
    Product("Brochas", 25.000, R.drawable.brochas),
    Product("Contorno", 35.000, R.drawable.contorno),
    Product("Corector", 12.000, R.drawable.corrector),
    Product("Gloss", 6.000, R.drawable.gloss),
        Product("Iluminador", 18.000, R.drawable.iluminador),
    Product("Labial", 14.000, R.drawable.labial),
    Product("Paleta", 50.000, R.drawable.paleta),
    Product("Prime", 25.000, R.drawable.prime)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_productos)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = ProductoAdapter(listaProductos)
        return view
    }

}