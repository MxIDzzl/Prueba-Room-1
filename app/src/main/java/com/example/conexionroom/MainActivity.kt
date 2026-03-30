package com.example.conexionroom

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.conexionroom.data.AppDatabase
import com.example.conexionroom.data.Veterinaria_bd

class MainActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etRaza: EditText
    private lateinit var etEdad: EditText

    private lateinit var btnRegistrar: Button
    private lateinit var btnBuscar: Button
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button

    private var mascotaActual: Veterinaria_bd? = null

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // EditTexts
        etNombre = findViewById(R.id.etNombre)
        etRaza = findViewById(R.id.etRaza)
        etEdad = findViewById(R.id.etEdad)

        // Buttons
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnBuscar = findViewById(R.id.btnBuscar)
        btnEditar = findViewById(R.id.btnEditar)
        btnEliminar = findViewById(R.id.btnEliminar)

        db = AppDatabase.getDatabase(this)
        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val raza = etRaza.text.toString().trim()
            val edadText = etEdad.text.toString().trim()

            if (!validarDatos(nombre, raza, edadText)) return@setOnClickListener

            val edad = edadText.toInt()
            db.mascotaDao().insertarMascota(Veterinaria_bd(nombre = nombre, raza = raza, edad = edad))
            Toast.makeText(this, "Mascota registrada", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        }
        btnBuscar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Escribe un nombre para buscar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val lista = db.mascotaDao().buscarMascota(nombre)
            if (lista.isNotEmpty()) {
                mascotaActual = lista[0]
                etRaza.setText(mascotaActual!!.raza)
                etEdad.setText(mascotaActual!!.edad.toString())
                Toast.makeText(this, "Mascota encontrada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se encontró la mascota", Toast.LENGTH_SHORT).show()
                limpiarCampos()
                mascotaActual = null
            }
        }
        btnEditar.setOnClickListener {
            val mascota = mascotaActual
            if (mascota == null) {
                Toast.makeText(this, "Primero busca la mascota", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val nuevoNombre = etNombre.text.toString().trim()
            val nuevaRaza = etRaza.text.toString().trim()
            val nuevaEdadText = etEdad.text.toString().trim()

            if (!validarDatos(nuevoNombre, nuevaRaza, nuevaEdadText)) return@setOnClickListener

            val nuevaEdad = nuevaEdadText.toInt()
            val mascotaEditada = Veterinaria_bd(
                id = mascota.id,
                nombre = nuevoNombre,
                raza = nuevaRaza,
                edad = nuevaEdad
            )
            db.mascotaDao().actualizarMascota(mascotaEditada)
            mascotaActual = mascotaEditada
            Toast.makeText(this, "Mascota editada", Toast.LENGTH_SHORT).show()
        }
        btnEliminar.setOnClickListener {
            val mascota = mascotaActual
            if (mascota == null) {
                Toast.makeText(this, "Primero busca la mascota", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            db.mascotaDao().eliminarMascota(mascota)
            Toast.makeText(this, "Mascota eliminada", Toast.LENGTH_SHORT).show()
            limpiarCampos()
            mascotaActual = null
        }
    }
    private fun validarDatos(nombre: String, raza: String, edad: String): Boolean {
        if (nombre.isEmpty() || raza.isEmpty() || edad.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun limpiarCampos() {
        etNombre.text.clear()
        etRaza.text.clear()
        etEdad.text.clear()
    }
}