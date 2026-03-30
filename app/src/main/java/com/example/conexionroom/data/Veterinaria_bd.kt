package com.example.conexionroom.data
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cliente")
class Veterinaria_bd (

    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val nombre: String,
    val raza: String,
    val edad: Int

)