package com.example.conexionroom.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReservationDao {
    @Insert
    fun insert(reservation: Reservation)

    @Update
    fun update(reservation: Reservation)

    @Query("SELECT * FROM reservations WHERE userId = :userId ORDER BY id DESC")
    fun getByUser(userId: Int): List<Reservation>

    @Query("SELECT * FROM reservations ORDER BY id DESC")
    fun getAll(): List<Reservation>

    @Query("SELECT * FROM reservations WHERE id = :id LIMIT 1")
    fun getById(id: Int): Reservation?
}
