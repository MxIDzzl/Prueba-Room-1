package com.example.conexionroom

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.conexionroom.data.AppDatabase
import com.example.conexionroom.data.Reservation

class ReservationActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        db = AppDatabase.getDatabase(this)

        val userId = intent.getIntExtra(HomeActivity.EXTRA_USER_ID, -1)
        if (userId <= 0) {
            finish()
            return
        }

        val etDate = findViewById<EditText>(R.id.etDate)
        val etTime = findViewById<EditText>(R.id.etTime)
        val etGuests = findViewById<EditText>(R.id.etGuests)

        findViewById<Button>(R.id.btnSaveReservation).setOnClickListener {
            val date = etDate.text.toString().trim()
            val time = etTime.text.toString().trim()
            val guests = etGuests.text.toString().toIntOrNull()

            if (date.isEmpty() || time.isEmpty() || guests == null || guests !in 1..12) {
                Toast.makeText(this, "Completa datos validos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.reservationDao().insert(
                Reservation(userId = userId, date = date, time = time, guests = guests)
            )
            Toast.makeText(this, "Reservacion creada", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
