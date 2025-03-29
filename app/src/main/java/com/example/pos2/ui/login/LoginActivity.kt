package com.example.pos2.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pos2.MainActivity
import com.example.pos2.databinding.ActivityLoginBinding
import com.example.pos2.ui.dashboard.DashboardActivity
import com.example.pos2.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val registerButton = binding.registerButton

        login.setOnClickListener {
            val userInput = username.text.toString().trim()
            val passInput = password.text.toString().trim()

            if (validateLogin(userInput, passInput)) {
                if (userInput.equals("Admin", ignoreCase = true)) {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                finish()
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // ฟังก์ชันตรวจสอบบัญชีที่ลงทะเบียนไว้
    private fun validateLogin(username: String, password: String): Boolean {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val savedPassword = sharedPreferences.getString(username, null) // ดึงรหัสผ่านที่บันทึกไว้

        return savedPassword != null && savedPassword == password // ตรวจสอบว่าตรงกับข้อมูลที่บันทึกไว้
    }
}
