package com.example.conexionroom.data

import androidx.room.*

@Dao
interface A_Data {

    @Insert
    fun insertarMascota(mascota: Veterinaria_bd)

    @Query("SELECT * FROM cliente WHERE nombre = :nombreMascota")
    fun buscarMascota(nombreMascota: String): List<Veterinaria_bd>

    @Update
    fun actualizarMascota(mascota: Veterinaria_bd)

    @Delete
    fun eliminarMascota(mascota: Veterinaria_bd)
}