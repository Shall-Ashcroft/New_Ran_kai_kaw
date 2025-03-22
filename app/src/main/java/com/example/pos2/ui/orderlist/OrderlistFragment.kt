package com.example.pos2.ui.orderlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pos2.databinding.FragmentOrlistBinding
import com.example.pos2.ui.Creor.Order // Import your Order class

class OrderListFragment : Fragment() {

    private var _binding: FragmentOrlistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create a list of Order objects
        val orderList = listOf(
            Order("Product 1", 2, 100.0f),
            Order("Product 2", 1, 200.0f),
            Order("Product 3", 3, 150.0f),
            Order("Product 4", 1, 250.0f)
        )

        // Initialize the adapter with the order list
        val adapter = OrderAdapter(orderList) { position ->
            // Handle item click, remove or any other functionality you want
        }

        // Set up RecyclerView with the adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
