package com.example.conexionroom

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.conexionroom.data.AppDatabase

class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        db = AppDatabase.getDatabase(this)

        val userId = intent.getIntExtra(HomeActivity.EXTRA_USER_ID, -1)
        val user = db.userDao().getById(userId)
        if (user == null || !user.isAdmin) {
            finish()
            return
        }

        val rows = db.reservationDao().getAll().map {
            "Reserva #${it.id} | usuario ${it.userId} | ${it.date} ${it.time} | ${it.guests} pers | ${it.status}"
        }

        findViewById<ListView>(R.id.lvAllReservations).adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, rows)
    }
}
