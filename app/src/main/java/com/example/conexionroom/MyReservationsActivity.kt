package com.example.conexionroom

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.conexionroom.data.AppDatabase

class MyReservationsActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reservations)

        db = AppDatabase.getDatabase(this)

        val userId = intent.getIntExtra(HomeActivity.EXTRA_USER_ID, -1)
        if (userId <= 0) {
            finish()
            return
        }

        loadReservations(userId)

        findViewById<Button>(R.id.btnCancelSelected).setOnClickListener {
            val listView = findViewById<ListView>(R.id.lvReservations)
            val selectedPosition = listView.checkedItemPosition
            if (selectedPosition == ListView.INVALID_POSITION) {
                Toast.makeText(this, "Selecciona una reservacion", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val reservations = db.reservationDao().getByUser(userId)
            val reservation = reservations.getOrNull(selectedPosition) ?: return@setOnClickListener
            db.reservationDao().update(reservation.copy(status = "CANCELADA"))
            loadReservations(userId)
            Toast.makeText(this, "Reservacion cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadReservations(userId: Int) {
        val reservations = db.reservationDao().getByUser(userId)
        val rows = reservations.map {
            "#${it.id} ${it.date} ${it.time} - ${it.guests} personas (${it.status})"
        }

        val listView = findViewById<ListView>(R.id.lvReservations)
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, rows)
    }
}
