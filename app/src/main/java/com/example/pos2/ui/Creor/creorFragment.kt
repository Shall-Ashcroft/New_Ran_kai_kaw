package com.example.pos2.ui.Creor

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pos2.databinding.FragmentCreorBinding
import com.example.pos2.ui.orderlist.OrderAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Order(val productName: String, val quantity: Int, val price: Float)

class CreorFragment : Fragment() {

    private var _binding: FragmentCreorBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderAdapter: OrderAdapter
    private val orderList = mutableListOf<Order>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderAdapter = OrderAdapter(orderList) { position ->
            removeOrder(position)
        }

        binding.rvOrderList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrderList.adapter = orderAdapter

        loadOrders()

        binding.btnCreateOrder.setOnClickListener {
            addOrder()
        }
    }

    private fun addOrder() {
        val productName = binding.productName.text.toString().trim()
        val quantityStr = binding.quantity.text.toString().trim()
        val priceStr = binding.price.text.toString().trim()

        if (productName.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(requireContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val quantity = quantityStr.toInt()
            val price = priceStr.toFloat()

            if (quantity <= 0 || price <= 0) {
                Toast.makeText(requireContext(), "จำนวนและราคาต้องมากกว่าศูนย์", Toast.LENGTH_SHORT).show()
                return
            }

            val newOrder = Order(productName, quantity, price)
            orderList.add(newOrder)
            saveOrders()

            orderAdapter.notifyItemInserted(orderList.size - 1)
            Toast.makeText(requireContext(), "เพิ่มออเดอร์สำเร็จ", Toast.LENGTH_SHORT).show()

            // เคลียร์ช่องกรอกข้อมูล
            binding.productName.text.clear()
            binding.quantity.text.clear()
            binding.price.text.clear()

        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), "กรุณากรอกจำนวนและราคาที่ถูกต้อง", Toast.LENGTH_SHORT).show()
        }
    }


    private fun removeOrder(position: Int) {
        if (position < 0 || position >= orderList.size) return // ป้องกัน IndexOutOfBoundsException

        orderList.removeAt(position) // ลบออเดอร์ออกจากลิสต์
        saveOrders() // บันทึกข้อมูลใหม่
        orderAdapter.notifyItemRemoved(position) // แจ้ง Adapter ว่ารายการถูกลบ
        orderAdapter.notifyItemRangeChanged(position, orderList.size) // อัปเดตรายการหลังจากลบ

        Toast.makeText(requireContext(), "ลบออเดอร์แล้ว", Toast.LENGTH_SHORT).show()
    }



    private fun saveOrders() {
        val sharedPreferences = requireActivity().getSharedPreferences("order_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(orderList)
        editor.putString("orders", json)
        editor.apply()
    }

    private fun loadOrders() {
        val sharedPreferences = requireActivity().getSharedPreferences("order_data", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("orders", null)
        val type = object : TypeToken<MutableList<Order>>() {}.type

        try {
            // ตรวจสอบว่า json ไม่เป็น null และสามารถแปลงเป็น List ได้
            if (json != null) {
                orderList.clear()
                orderList.addAll(gson.fromJson(json, type) ?: mutableListOf())
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "เกิดข้อผิดพลาดในการโหลดข้อมูล", Toast.LENGTH_SHORT).show()
        }

        orderAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
