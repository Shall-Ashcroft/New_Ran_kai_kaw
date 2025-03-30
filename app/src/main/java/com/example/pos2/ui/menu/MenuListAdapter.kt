package com.example.pos2.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pos2.R

class MenuListAdapter(
    private val menuList: MutableList<String>,
    private val onDeleteClick: (String) -> Unit  // ใช้ String แทนการรับ position
) : RecyclerView.Adapter<MenuListAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val menuNameTextView: TextView = itemView.findViewById(R.id.menu_text)
        val deleteButton: View = itemView.findViewById(R.id.delete_button)

        init {
            deleteButton.setOnClickListener {
                val menuName = menuNameTextView.text.toString() // ดึงชื่อเมนูจาก TextView
                onDeleteClick(menuName)  // ส่งชื่อเมนูที่ต้องการลบไป
                Toast.makeText(itemView.context, "Menu deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuName = menuList[position]
        holder.menuNameTextView.text = menuName
    }

    override fun getItemCount(): Int = menuList.size
}
