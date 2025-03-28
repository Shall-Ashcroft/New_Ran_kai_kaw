package com.example.pos2.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pos2.MainActivity
import com.example.pos2.databinding.ActivityLoginBinding
import com.example.pos2.ui.dashboard.DashboardActivity
import com.example.pos2.ui.register.RegisterActivity // เพิ่มการนำเข้า RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val registerButton = binding.registerButton // รับการอ้างอิงของปุ่ม Register

        // การตั้งค่า Login Button
        login.setOnClickListener {
            val userInput = username.text.toString().trim()
            val passInput = password.text.toString().trim()

            if (userInput.isNotEmpty() && passInput.isNotEmpty()) {
                if (userInput.equals("Admin", ignoreCase = true)) {
                    // ถ้าเป็น Admin ให้ไปหน้า Dashboard
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                } else {
                    // ถ้าไม่ใช่ Admin ให้ไปหน้า MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                finish()
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        // การตั้งค่า Register Button
        registerButton.setOnClickListener {
            // เมื่อคลิกปุ่ม Register ให้เปิดหน้า RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
