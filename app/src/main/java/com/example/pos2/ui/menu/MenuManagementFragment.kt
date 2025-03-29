package com.example.pos2.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos2.R
import com.example.pos2.ui.menu.MenuListAdapter


class MenuManagementFragment : Fragment() {

    private lateinit var btnAddMenu: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var menuListAdapter: MenuListAdapter

    // รายการเมนู
    private val menuList = mutableListOf("Pizza", "Burger", "Pasta")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_menu_management, container, false)

        btnAddMenu = view.findViewById(R.id.btnAddMenu)
        recyclerView = view.findViewById(R.id.rvMenuList)

        recyclerView.layoutManager = LinearLayoutManager(context)
        menuListAdapter = MenuListAdapter(menuList)
        recyclerView.adapter = menuListAdapter

        btnAddMenu.setOnClickListener {
            if (isAdmin()) {
                menuList.add("New Menu")
                menuListAdapter.notifyItemInserted(menuList.size - 1)
                Toast.makeText(context, "Menu added!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "You don't have permission to add menu.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun isAdmin(): Boolean {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", 0)
        return sharedPreferences.getBoolean("is_admin", false)
    }
}
