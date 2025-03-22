package com.example.pos2.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pos2.MainActivity
import com.example.pos2.databinding.ActivityDashboardBinding
import com.example.pos2.ui.orderlist.OrderAdapter
import com.example.pos2.ui.Creor.Order
import com.example.pos2.utils.PermissionManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var orderList: MutableList<Order>  // เปลี่ยนจาก List เป็น MutableList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the order list (replace this with actual data fetching logic)
        orderList = mutableListOf()  // คุณสามารถดึงข้อมูลจากฐานข้อมูลหรือ SharedPreferences ที่นี่
        loadDashboardData()

        // Initialize RecyclerView with OrderAdapter
        val orderAdapter = OrderAdapter(orderList) { position ->
            // Handle delete order here, update the order list and notify the adapter
            deleteOrder(position)
        }
        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOrders.adapter = orderAdapter

        // Update Dashboard data (for demo purposes, replace with actual data)
        displayDashboardData()

        // Check if the user has admin permission
        val permissionManager = PermissionManager(this)
        if (!permissionManager.isAdmin()) {
            Toast.makeText(this, "You don't have permission to access this dashboard.", Toast.LENGTH_SHORT).show()
            // Navigate to MainActivity (or another screen) if the user isn't an admin
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Close the current activity
            return
        }
    }
    override fun onResume() {
        super.onResume()
        loadDashboardData()
    }

    private fun loadDashboardData() {
        val sharedPreferences = getSharedPreferences("order_data", Context.MODE_PRIVATE)
        val gson = Gson()

        // โหลดรายการออเดอร์
        val json = sharedPreferences.getString("orders", null)
        val type = object : TypeToken<List<Order>>() {}.type
        val orderList: List<Order> = gson.fromJson(json, type) ?: emptyList()

        // คำนวณยอดขายทั้งหมด
        val earnings = orderList.sumOf { it.price * it.quantity }

        // จำลองข้อมูลสต็อกวัตถุดิบ (ถ้าคุณมีระบบ stock จริงให้ดึงจากฐานข้อมูล)
        val ingredientsStock = 100 - orderList.sumOf { it.quantity } // ตัวอย่าง: วัตถุดิบลดตามจำนวนออเดอร์

        // จำลองจำนวน feedback (ถ้าคุณมีระบบ feedback ให้ดึงจากฐานข้อมูล)
        val feedbackCount = 5  // กำหนดค่าทดลอง, สามารถเปลี่ยนเป็นค่าจริงได้

        // อัปเดต UI
        binding.tvEarnings.text = "Earnings: ฿${"%.2f".format(earnings.toDouble())}"
        binding.tvIngredientsStock.text = "Ingredients in stock: $ingredientsStock"
        binding.tvTotalOrders.text = "Total orders: ${orderList.size}"
        binding.tvFeedback.text = "Feedback: $feedbackCount"
    }


    private fun deleteOrder(position: Int) {
        // Delete the order and update the RecyclerView
        orderList.removeAt(position)
        binding.recyclerViewOrders.adapter?.notifyItemRemoved(position)
        displayDashboardData()  // Update the dashboard data after deletion
    }
    private fun updateOrderCount() {
        val sharedPreferences = getSharedPreferences("order_data", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("orders", null)
        val type = object : TypeToken<List<Order>>() {}.type
        val orderList: List<Order> = gson.fromJson(json, type) ?: emptyList()

        binding.tvTotalOrders.text = "Total Orders: ${orderList.size}"
    }


    private fun displayDashboardData() {
        val earnings = 15000.0
        val ingredientsStock = 50
        val totalOrders = orderList.size  // จำนวนออเดอร์จาก orderList
        val feedbackCount = 35

        // แสดงข้อมูลใน UI
        binding.tvEarnings.text = "Earnings: ฿${"%.2f".format(earnings.toDouble())}"
        binding.tvIngredientsStock.text = "Ingredients in stock: $ingredientsStock"
        binding.tvTotalOrders.text = "Total orders: $totalOrders"
        binding.tvFeedback.text = "Feedback: $feedbackCount"
    }
}