package com.example.conexionroom.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface VeterinariaDao {
    @Insert
    suspend fun insert(cliente: Veterinaria_bd)

    @Query("SELECT * FROM cliente")
    suspend fun getAllClientes(): List<Veterinaria_bd>

    @Update
    suspend fun update(cliente: Veterinaria_bd)

    @Delete
    suspend fun delete(cliente: Veterinaria_bd)

}