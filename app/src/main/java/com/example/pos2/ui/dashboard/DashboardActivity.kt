    package com.example.pos2.ui.dashboard

    import android.content.Context
    import android.content.Intent
    import android.os.Bundle
    import androidx.appcompat.app.AppCompatActivity
    import com.example.pos2.databinding.ActivityDashboardBinding
    import com.example.pos2.ui.Creor.Order
    import com.example.pos2.ui.login.LoginActivity
    import com.google.gson.Gson
    import com.google.gson.reflect.TypeToken

    class DashboardActivity : AppCompatActivity() {

        private lateinit var binding: ActivityDashboardBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = ActivityDashboardBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.btnLogout.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

            loadDashboardData()
        }

        // Method to load dashboard data
        private fun loadDashboardData() {
            val sharedPreferences = getSharedPreferences("order_data", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("orders", null)
            val type = object : TypeToken<List<Order>>() {}.type
            val orderList: MutableList<Order> = gson.fromJson(json, type) ?: mutableListOf()

            val earnings = orderList.sumOf { it.price * it.quantity }
            val ingredientsStock = 100 - orderList.sumOf { it.quantity }
            val feedbackCount = 5

            binding.tvEarnings.text = "Earnings: ฿${"%.2f".format(earnings.toDouble())}"
            binding.tvIngredientsStock.text = "Ingredients in stock: $ingredientsStock"
            binding.tvTotalOrders.text = "Total orders: ${orderList.size}"
            binding.tvFeedback.text = "Feedback: $feedbackCount"
        }
    }
