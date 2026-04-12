package com.example.conexionroom

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.conexionroom.data.AppDatabase

class HomeActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = AppDatabase.getDatabase(this)

        val userId = intent.getIntExtra(EXTRA_USER_ID, -1)
        if (userId <= 0) {
            finish()
            return
        }

        val user = db.userDao().getById(userId) ?: run {
            finish()
            return
        }

        findViewById<TextView>(R.id.tvWelcome).text = "Hola, ${user.name}"

        findViewById<Button>(R.id.btnCreateReservation).setOnClickListener {
            startActivity(Intent(this, ReservationActivity::class.java).putExtra(EXTRA_USER_ID, userId))
        }

        findViewById<Button>(R.id.btnMyReservations).setOnClickListener {
            startActivity(Intent(this, MyReservationsActivity::class.java).putExtra(EXTRA_USER_ID, userId))
        }

        val adminButton = findViewById<Button>(R.id.btnAdminPanel)
        adminButton.isEnabled = user.isAdmin
        adminButton.alpha = if (user.isAdmin) 1f else 0.5f
        adminButton.setOnClickListener {
            startActivity(Intent(this, RestaurantDetailActivity::class.java).putExtra(EXTRA_USER_ID, userId))
        }

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            finish()
        }
    }

    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
    }
}
