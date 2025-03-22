package com.example.pos2.ui.orderlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pos2.databinding.FragmentOrlistBinding
import com.example.pos2.ui.Creor.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OrderListFragment : Fragment() {

    private var _binding: FragmentOrlistBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderAdapter: OrderAdapter
    private val orderList = mutableListOf<Order>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ตั้งค่า Adapter
        orderAdapter = OrderAdapter(orderList) { position -> removeOrder(position) }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = orderAdapter

        // โหลดข้อมูลหลังจากที่ Adapter ถูกสร้างแล้ว
        loadOrders()
    }


    private fun loadOrders() {
        val sharedPreferences = requireActivity().getSharedPreferences("order_data", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("orders", null)
        val type = object : TypeToken<MutableList<Order>>() {}.type
        orderList.clear()
        orderList.addAll(gson.fromJson(json, type) ?: mutableListOf())

        // อัปเดต UI
        orderAdapter.notifyDataSetChanged()
    }

    private fun removeOrder(position: Int) {
        if (position >= 0 && position < orderList.size) {
            orderList.removeAt(position)
            saveOrders() // บันทึกการเปลี่ยนแปลง
            orderAdapter.notifyItemRemoved(position)
        }
    }

    private fun saveOrders() {
        val sharedPreferences = requireActivity().getSharedPreferences("order_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(orderList)
        editor.putString("orders", json)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        loadOrders() // โหลดข้อมูลใหม่เมื่อกลับมาเปิดหน้า
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
