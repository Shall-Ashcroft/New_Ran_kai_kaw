package com.example.pos2.ui.dashboard

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pos2.R
import com.example.pos2.databinding.FragmentDashboardBinding
import com.example.pos2.ui.Creor.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up the logout button (this may be handled elsewhere, but you can add it here too)
        binding.btnLogout.setOnClickListener {
            // Handle logout here if needed
        }

        // Load dashboard data
        loadDashboardData()

        return root
    }

    // Method to load dashboard data
    private fun loadDashboardData() {
        val sharedPreferences = requireContext().getSharedPreferences("order_data", Context.MODE_PRIVATE)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
