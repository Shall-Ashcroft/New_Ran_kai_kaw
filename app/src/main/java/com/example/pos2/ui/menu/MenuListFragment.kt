package com.example.pos2.ui.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pos2.databinding.FragmentMenuListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MenuListFragment : Fragment() {

    private var _binding: FragmentMenuListBinding? = null
    private val binding get() = _binding!!

    private lateinit var menuListAdapter: MenuListAdapter
    private val menuList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ตั้งค่า Adapter
        menuListAdapter = MenuListAdapter(menuList) { menuName -> removeMenu(menuName) }
        binding.rvMenuList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMenuList.adapter = menuListAdapter

        // โหลดข้อมูลจาก SharedPreferences
        loadMenuData()
    }

    private fun loadMenuData() {
        val sharedPreferences = requireActivity().getSharedPreferences("menu_data", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("menu_list", null)  // อ่านข้อมูลจาก SharedPreferences
        val type = object : TypeToken<MutableList<String>>() {}.type
        menuList.addAll(gson.fromJson(json, type) ?: mutableListOf())  // เพิ่มข้อมูลใหม่ลงใน list

        // อัปเดต UI
        menuListAdapter.notifyDataSetChanged()
    }


    private fun removeMenu(menuName: String) {
        menuList.remove(menuName)
        saveMenuData() // บันทึกการเปลี่ยนแปลง
        menuListAdapter.notifyDataSetChanged() // แจ้ง Adapter ให้ทราบว่าข้อมูลมีการเปลี่ยนแปลง
    }

    private fun saveMenuData() {
        val sharedPreferences = requireActivity().getSharedPreferences("menu_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(menuList)  // แปลง list ของเมนูเป็น JSON
        editor.putString("menu_list", json)  // บันทึกข้อมูลลงใน SharedPreferences
        editor.apply()  // ใช้ apply เพื่อบันทึกข้อมูลทันที
    }




    override fun onResume() {
        super.onResume()
        loadMenuData() // โหลดข้อมูลเมื่อกลับมาเปิดหน้า
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
