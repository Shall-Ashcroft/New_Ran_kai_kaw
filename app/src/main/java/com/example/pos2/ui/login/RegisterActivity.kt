package com.example.pos2.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pos2.databinding.ActivityRegisterBinding
import com.example.pos2.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.registerUsername
        val password = binding.registerPassword
        val registerButton = binding.registerSubmitButton

        registerButton.setOnClickListener {
            val userInput = username.text.toString().trim()
            val passInput = password.text.toString().trim()

            if (userInput.isNotEmpty() && passInput.isNotEmpty()) {
                if (registerUser(userInput, passInput)) {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(username: String, password: String): Boolean {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        if (sharedPreferences.contains(username)) {
            return false // ถ้าชื่อผู้ใช้มีอยู่แล้วให้ return false
        }

        val editor = sharedPreferences.edit()
        editor.putString(username, password) // บันทึกชื่อผู้ใช้และรหัสผ่าน
        editor.apply()

        return true // ลงทะเบียนสำเร็จ
    }
}
