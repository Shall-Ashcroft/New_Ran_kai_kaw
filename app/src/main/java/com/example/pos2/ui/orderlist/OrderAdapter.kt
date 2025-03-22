package com.example.pos2.ui.orderlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pos2.databinding.ItemOrderBinding
import com.example.pos2.ui.Creor.Order

class OrderAdapter(
    private val orderList: List<Order>,  // เปลี่ยนจาก List เป็น MutableList
    private val onDeleteClick: (Int) -> Unit // รับ Callback สำหรับลบ
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order)

        // กดปุ่มลบ เพื่อลบออเดอร์
        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int = orderList.size

    inner class OrderViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.productName.text = order.productName
            binding.quantity.text = order.quantity.toString()
            binding.price.text = order.price.toString()
        }
    }
}


