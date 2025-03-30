package com.example.pos2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pos2.databinding.ActivityMainBinding
import com.example.pos2.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_creor, R.id.nav_orlist, R.id.nav_dashboard, R.id.nav_logout),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Set up the navigation menu item for logout
        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            logout()
            true
        }

        // ตรวจสอบสถานะ Admin และโหลด Fragment ที่เหมาะสมเมื่อแอปเปิด
        if (savedInstanceState == null) {
            if (checkIfAdmin()) {
                // ถ้าเป็น Admin ให้ไปที่ DashboardFragment
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.nav_dashboard)
            } else {
                // ถ้าไม่ใช่ Admin ให้ไปที่ CreateOrderFragment
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.nav_creor)
            }
        }

        // ตรวจสอบและแสดงเมนูใน NavigationView ตามสถานะ Admin
        checkUserRole(navView)
    }

    private fun checkUserRole(navView: NavigationView) {
        val isAdmin = checkIfAdmin()  // ฟังก์ชันที่ตรวจสอบว่าเป็น Admin หรือไม่

        // เปลี่ยนการแสดงผลของเมนูใน Navigation Drawer
        navView.menu.findItem(R.id.nav_creor).isVisible = !isAdmin  // ซ่อนเมนู CreateOrder สำหรับ Admin
        navView.menu.findItem(R.id.nav_orlist).isVisible = !isAdmin  // ซ่อนเมนู Order List สำหรับ Admin
        navView.menu.findItem(R.id.nav_menu_management).isVisible = isAdmin // แสดงเมนู Menu Management เฉพาะ Admin
        navView.menu.findItem(R.id.nav_dashboard).isVisible = isAdmin // แสดงเมนู Dashboard เฉพาะ Admin
    }

    private fun checkIfAdmin(): Boolean {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_admin", false)  // อ่านค่าจาก SharedPreferences
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
