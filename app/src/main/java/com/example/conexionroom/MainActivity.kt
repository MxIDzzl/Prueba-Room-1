package com.example.conexionroom

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.conexionroom.data.AppDatabase
import com.example.conexionroom.data.User

class MainActivity : AppCompatActivity() {

    private lateinit var etLoginEmail: EditText
    private lateinit var etLoginPassword: EditText
    private lateinit var etRegisterName: EditText
    private lateinit var etRegisterEmail: EditText
    private lateinit var etRegisterPassword: EditText
    private lateinit var etRegisterConfirmPassword: EditText

    private lateinit var btnLogin: Button
    private lateinit var btnCreateAccount: Button
    private lateinit var tvShowRegister: TextView
    private lateinit var tvShowLogin: TextView
    private lateinit var loginContainer: View
    private lateinit var registerContainer: View

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getDatabase(this)

        etLoginEmail = findViewById(R.id.etLoginEmail)
        etLoginPassword = findViewById(R.id.etLoginPassword)
        etRegisterName = findViewById(R.id.etRegisterName)
        etRegisterEmail = findViewById(R.id.etRegisterEmail)
        etRegisterPassword = findViewById(R.id.etRegisterPassword)
        etRegisterConfirmPassword = findViewById(R.id.etRegisterConfirmPassword)

        btnLogin = findViewById(R.id.btnLogin)
        btnCreateAccount = findViewById(R.id.btnCreateAccount)
        tvShowRegister = findViewById(R.id.tvShowRegister)
        tvShowLogin = findViewById(R.id.tvShowLogin)
        loginContainer = findViewById(R.id.loginContainer)
        registerContainer = findViewById(R.id.registerContainer)

        showLogin()

        btnLogin.setOnClickListener { loginUser() }
        btnCreateAccount.setOnClickListener { registerUser() }
        tvShowRegister.setOnClickListener { showRegister() }
        tvShowLogin.setOnClickListener { showLogin() }
    }

    private fun registerUser() {
        val name = etRegisterName.text.toString().trim()
        val email = etRegisterEmail.text.toString().trim().lowercase()
        val password = etRegisterPassword.text.toString().trim()
        val confirmPassword = etRegisterConfirmPassword.text.toString().trim()

        when {
            name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                showToast("Completa todos los campos")
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Ingresa un correo valido")
                return
            }
            password != confirmPassword -> {
                showToast("Las contrasenas no coinciden")
                return
            }
            db.userDao().getUserByEmail(email) != null -> {
                showToast("Ese correo ya esta registrado")
                return
            }
        }

        db.userDao().insertUser(
            User(
                name = name,
                email = email,
                password = password
            )
        )

        clearRegisterFields()
        etLoginEmail.setText(email)
        showLogin()
        showToast("Cuenta creada correctamente")
    }

    private fun loginUser() {
        val email = etLoginEmail.text.toString().trim().lowercase()
        val password = etLoginPassword.text.toString().trim()

        when {
            email.isEmpty() || password.isEmpty() -> {
                showToast("Completa correo y contrasena")
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Ingresa un correo valido")
                return
            }
        }

        val user = db.userDao().login(email, password)
        if (user == null) {
            val existingUser = db.userDao().getUserByEmail(email)
            if (existingUser == null) {
                etRegisterEmail.setText(email)
                showRegister()
                showToast("No tienes cuenta. Registrate")
            } else {
                showToast("Correo o contrasena incorrectos")
            }
            return
        }

        startActivity(
            Intent(this, HomeActivity::class.java)
                .putExtra(HomeActivity.EXTRA_USER_ID, user.id)
        )
    }

    private fun showLogin() {
        loginContainer.visibility = View.VISIBLE
        registerContainer.visibility = View.GONE
        etLoginPassword.text.clear()
    }

    private fun showRegister() {
        registerContainer.visibility = View.VISIBLE
        loginContainer.visibility = View.GONE
    }

    private fun clearRegisterFields() {
        etRegisterName.text.clear()
        etRegisterEmail.text.clear()
        etRegisterPassword.text.clear()
        etRegisterConfirmPassword.text.clear()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}