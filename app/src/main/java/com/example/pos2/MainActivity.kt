package com.example.pos2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
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

        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            logout()
            true
        }

        // ตรวจสอบสิทธิ์การเข้าถึงเมนู (Admin หรือไม่)
        checkUserRole(navView)
    }

    private fun checkUserRole(navView: NavigationView) {
        val isAdmin = checkIfAdmin()  // ฟังก์ชันที่ตรวจสอบว่าเป็น Admin หรือไม่

        // เปลี่ยนการแสดงผลเมนูใน Navigation Drawer
        navView.menu.findItem(R.id.nav_menu_management).isVisible = isAdmin
        navView.menu.findItem(R.id.nav_dashboard).isVisible = !isAdmin  // ซ่อน Dashboard สำหรับ Admin
    }

    private fun checkIfAdmin(): Boolean {
        // ตรวจสอบว่าเป็น Admin หรือไม่
        // ใช้ SharedPreferences หรือวิธีการอื่นๆ ในการเก็บข้อมูลผู้ใช้
        return true // หรือ false หากผู้ใช้ไม่ใช่ Admin
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
