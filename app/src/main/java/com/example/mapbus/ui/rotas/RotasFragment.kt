package com.example.mapbus.ui.rotas

import RotaAdpter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapbus.R

import com.example.mapbus.dataSource.DataSource
import com.example.mapbus.databinding.FragmentRotasBinding

class RotasFragment : Fragment() {

    private var _binding: FragmentRotasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRotasBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {

        initRecycle(binding.root)
        super.onResume()
    }
    fun initRecycle(root : View){
        val rotasRecyclerView = root.findViewById<RecyclerView>(R.id.rotasRecycle)
        rotasRecyclerView.layoutManager = LinearLayoutManager(root.context,LinearLayoutManager.VERTICAL,false)
        val dados = DataSource.getRota()
        rotasRecyclerView.adapter = RotaAdpter(dados)
    }
}