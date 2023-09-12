package com.example.mapbus.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.mapbus.R
import com.example.mapbus.dataSource.api.ApiServiceBuilder
import com.example.mapbus.dataSource.api.Localizacao
import com.example.mapbus.databinding.FragmentHomeBinding
import com.example.mapbus.model.Rota
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        binding.toggleButton3.setOnClickListener {
            getCurrentLocation()
            obterDadosDaApi()
        }



    }

    @RequiresApi(Build.VERSION_CODES.O)
    private  fun getDate(): String? {
        val timestamp = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"))
            .format(DateTimeFormatter.ofPattern("MM.dd.yyy hh.mm.ss a"))

        return timestamp // 01.01.2017 01.59.13 pm
    }





    private fun enviarDadosLocalizacao(localizacao: Localizacao) {


        val call = ApiServiceBuilder.apiService.enviarLocalizacao(localizacao)
        call.enqueue(object : Callback<Localizacao> {
            override fun onResponse(call: Call<Localizacao>, response: Response<Localizacao>) {
                if (response.isSuccessful) {
                    val resposta = response.body()
                    println("Resposta enviada: $resposta")
                } else {
                    val erro = response.errorBody()?.string()
                    println("Erro na resposta: $erro")
                }
            }

            override fun onFailure(call: Call<Localizacao>, t: Throwable) {
                println("Falha na requisição: ${t.message}")
            }
        })
    }

    private fun obterDadosDaApi() {
        val call = ApiServiceBuilder.apiService.obterLocalizacao()
        call.enqueue(object : Callback<Localizacao> {
            override fun onResponse(call: Call<Localizacao>, response: Response<Localizacao>) {
                if (response.isSuccessful) {
                    val localizacao = response.body()
                    println("Localização recebida: $localizacao")

                } else {
                    val erro = response.errorBody()?.string()
                    println("Erro na resposta: $erro")
                }
            }

            override fun onFailure(call: Call<Localizacao>, t: Throwable) {
                println("Falha na requisição: ${t.message}")
            }
        })
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }

                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        Toast.makeText(requireContext(), "Localização nula", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), "Sucesso", Toast.LENGTH_LONG).show()
                        val horario = getDate()
                        val localizacao = Localizacao(horario,location.latitude,location.longitude,0)
                        enviarDadosLocalizacao(localizacao)
                        requireActivity().findViewById<TextView>(R.id.signs).text = " Atual ponto - ônibus "
                        encontrarParadaMaisRecente()



                    }
                }
            } else {
                Toast.makeText(requireContext(), "Ligue a localização!", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    private fun checkPermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Granted", Toast.LENGTH_LONG).show()
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun encontrarParadaMaisRecente(){
        val call = ApiServiceBuilder.apiService.obterRota()
        call.enqueue(object : Callback<Rota> {
            override fun onResponse(call: Call<Rota>, response: Response<Rota>) {
                if (response.isSuccessful) {
                    val rota = response.body()
                    println(response.body())
                    if (rota!=null) {
                       var nome = rota.nome_ponto
                        requireActivity().findViewById<TextView>(R.id.nomePonto).text = nome
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

