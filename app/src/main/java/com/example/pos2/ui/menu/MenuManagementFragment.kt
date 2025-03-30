package com.example.pos2.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pos2.databinding.FragmentMenuManagementBinding

class MenuManagementFragment : Fragment() {

    private lateinit var binding: FragmentMenuManagementBinding
    private lateinit var menuListAdapter: MenuListAdapter
    private val menuList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // เชื่อมโยงกับ ViewBinding
        binding = FragmentMenuManagementBinding.inflate(inflater, container, false)

        // กำหนด LayoutManager และ Adapter
        menuListAdapter = MenuListAdapter(menuList) { menuName ->
            // ลบเมนูจากรายการ
            menuList.remove(menuName)
            menuListAdapter.notifyDataSetChanged()  // แจ้ง Adapter ให้ทราบว่าข้อมูลมีการเปลี่ยนแปลง
        }

        binding.rvMenuList.layoutManager = LinearLayoutManager(context)
        binding.rvMenuList.adapter = menuListAdapter

        // เมื่อคลิกปุ่มเพิ่มเมนู
        binding.btnAddMenu.setOnClickListener {
            val newMenuName = binding.edtMenuName.text.toString().trim()
            if (newMenuName.isNotEmpty()) {
                menuList.add(newMenuName)  // เพิ่มชื่อเมนูใหม่เข้าไปในรายการ
                menuListAdapter.notifyItemInserted(menuList.size - 1)  // แจ้งให้ Adapter ทราบว่ามีรายการใหม่
                binding.edtMenuName.text.clear()  // ล้าง TextField
            }
        }

        return binding.root
    }
}
