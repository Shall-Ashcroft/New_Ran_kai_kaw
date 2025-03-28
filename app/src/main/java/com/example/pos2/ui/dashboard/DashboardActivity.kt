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
import com.example.pos2.ui.login.LoginActivity
import com.example.pos2.utils.PermissionManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var orderList: MutableList<Order>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ลบหรือซ่อนปุ่ม Create Order
        // binding.btnCreateOrder.setOnClickListener {
        //     // Action for creating order
        // }

        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        orderList = mutableListOf()
        loadDashboardData()

        val orderAdapter = OrderAdapter(orderList) { position ->
            deleteOrder(position)
        }
        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOrders.adapter = orderAdapter
    }

    // Method to load dashboard data
    private fun loadDashboardData() {
        val sharedPreferences = getSharedPreferences("order_data", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("orders", null)
        val type = object : TypeToken<List<Order>>() {}.type
        orderList = gson.fromJson(json, type) ?: mutableListOf()

        val earnings = orderList.sumOf { it.price * it.quantity }
        val ingredientsStock = 100 - orderList.sumOf { it.quantity }
        val feedbackCount = 5

        binding.tvEarnings.text = "Earnings: ฿${"%.2f".format(earnings.toDouble())}"
        binding.tvIngredientsStock.text = "Ingredients in stock: $ingredientsStock"
        binding.tvTotalOrders.text = "Total orders: ${orderList.size}"
        binding.tvFeedback.text = "Feedback: $feedbackCount"

        (binding.recyclerViewOrders.adapter as? OrderAdapter)?.updateOrders(orderList)
    }

    // Method to delete an order
    private fun deleteOrder(position: Int) {
        orderList.removeAt(position)

        val sharedPreferences = getSharedPreferences("order_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(orderList)
        editor.putString("orders", json)
        editor.apply()

        binding.recyclerViewOrders.adapter?.notifyItemRemoved(position)

        displayDashboardData()
    }

    private fun displayDashboardData() {
        val earnings = 15000.0
        val ingredientsStock = 50
        val totalOrders = orderList.size
        val feedbackCount = 35

        binding.tvEarnings.text = "Earnings: ฿${"%.2f".format(earnings)}"
        binding.tvIngredientsStock.text = "Ingredients in stock: $ingredientsStock"
        binding.tvTotalOrders.text = "Total orders: $totalOrders"
        binding.tvFeedback.text = "Feedback: $feedbackCount"
    }
}
