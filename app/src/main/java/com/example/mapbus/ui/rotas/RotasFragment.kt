package com.example.mapbus.ui.rotas

import RotaAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mapbus.R

import com.example.mapbus.dataSource.DataSource
import com.example.mapbus.dataSource.api.ApiServiceBuilder
import com.example.mapbus.databinding.FragmentRotasBinding
import com.example.mapbus.model.Rota
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RotasFragment : Fragment() {

    private var _binding: FragmentRotasBinding? = null
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var placeTextView: TextView


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
        swipeRefreshLayout = binding.container
        placeTextView = binding.root.findViewById<TextView>(R.id.emptyTextView)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {

        val rotasRecycle = initRecycle(binding.root)
       if (rotasRecycle.adapter?.itemCount==0){
            rotasRecycle.visibility = View.GONE
            placeTextView.visibility = View.VISIBLE
       }

        swipeRefreshLayout.setOnRefreshListener {
            println("recarregando")
            rotasRecycle.visibility = View.VISIBLE
            placeTextView.visibility = View.GONE
            encontrarParadaMaisRecente(rotasRecycle)
            rotasRecycle.adapter?.notifyDataSetChanged()
            // on below line we are setting is refreshing to false.
            swipeRefreshLayout.isRefreshing = false

        }

        super.onResume()
    }

    override fun onAttach(context: Context) {

        super.onAttach(context)
    }
    fun initRecycle(root : View):RecyclerView{

        val rotasRecyclerView = root.findViewById<RecyclerView>(R.id.rotasRecycle)
        rotasRecyclerView.layoutManager = LinearLayoutManager(root.context,LinearLayoutManager.VERTICAL,false)
        val dados = DataSource.getRota()
        rotasRecyclerView.adapter = RotaAdapter(dados)
        return rotasRecyclerView
    }
    fun encontrarParadaMaisRecente(recyclerView: RecyclerView){
        val call = ApiServiceBuilder.apiService.obterRota()
        call.enqueue(object : Callback<Rota> {
            override fun onResponse(call: Call<Rota>, response: Response<Rota>) {
                if (response.isSuccessful) {
                    val rota = response.body()
                    println(response.body())
                    if (rota!=null) {
                        DataSource.adicionarRota(rota)
                        recyclerView.adapter?.notifyItemInserted(0)
                    }
                } else {
                    val erro = response.errorBody()?.string()
                    println("Erro na resposta: $erro")
                }
            }
            override fun onFailure(call: Call<Rota>, t: Throwable) {
                println("Falha na requisição: ${t.message}")

            }

        })
    }
}