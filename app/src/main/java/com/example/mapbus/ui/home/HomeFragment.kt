package com.example.mapbus.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.mapbus.R
import com.example.mapbus.dataSource.api.ApiServiceBuilder
import com.example.mapbus.dataSource.api.Localizacao
import com.example.mapbus.dataSource.api.Paradas
import com.example.mapbus.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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

    override fun onResume() {
        super.onResume()
        binding.toggleButton3.setOnClickListener {
            getCurrentLocation()
            obterDadosDaApi()
        }



    }


    fun calcularDistancia(latitude1: Double, longitude1: Double, latitude2: Double, longitude2: Double): Double {
        val earthRadius = 6371000.0 // Raio médio da Terra em metros
        val dLat = Math.toRadians(latitude2 - latitude1)
        val dLng = Math.toRadians(longitude2 - longitude1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadius * c
    }

    fun encontrarParadaMaisProxima(latitudeEnviada: Double, longitudeEnviada: Double, paradas: List<Paradas>): Paradas? {
        var paradaProxima: Paradas? = null
        var menorDistancia = Double.MAX_VALUE

        for (parada in paradas) {
            val distancia = calcularDistancia(latitudeEnviada, longitudeEnviada, parada.latitude, parada.longitude)
            if (distancia <= 100.0 && distancia < menorDistancia) {
                menorDistancia = distancia
                paradaProxima = parada
            }
        }

        return paradaProxima
    }

    private fun calcularLocalizacao(localizacao: Localizacao): String? {

        val coordenadas = listOf(
            Paradas("09:00", -8.319598, -36.395205, 3, "AEB"),
            Paradas("09:02", -8.326801, -36.405290, 3, "UABJ"),
            Paradas("09:08", -8.344497, -36.413362, 3, "PRAÇA DAS CRIANÇAS"),
            Paradas("09:10", -8.342243, -36.416842, 3, "Escola DR. Sebastião Cabral"),
            Paradas("09:15", -8.345663, -36.434173, 3, "Trevo de Acesso"),
            Paradas("09:18", -8.339146, -36.432410, 3, "EREM João Monteiro"),
            Paradas("09:20", -8.337285, -36.430412, 3, "Posto PETROVIA"),
            Paradas("09:22", -8.337575, -36.425721, 3, "Centro"),
            Paradas("09:24", -8.337353, -36.419071, 3, "Fórum"),
            Paradas("09:28", -8.328507, -36.420694, 3, "Praça dos Eventos"),
            Paradas("09:29", -8.325523, -36.418549, 3, "Colégio Éxito"),
            Paradas("09:37", -8.333349, -36.417440, 3, "Escola Prof. Donino"),
            Paradas("09:41", -8.331854, -36.413564, 3, "Placa do Hospital Santa Fé"),
            Paradas("09:43", -8.326801, -36.405290, 3, "UABJ"),
            Paradas("09:45", -8.319598, -36.395205, 3, "AEB"),
            Paradas("8:90", -8.3227511,-36.3913617,3,"home")
        )

        val localizacaoProxima = encontrarParadaMaisProxima(localizacao.latitude, localizacao.longitude, coordenadas)
        if (localizacaoProxima != null) {
            return localizacaoProxima.nome
        }else{
            return "null"
        }

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
                    var nomeLocal = localizacao?.let { calcularLocalizacao(it) }
                    requireActivity().findViewById<TextView>(R.id.latitude).text = nomeLocal

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
                        var localizacao = Localizacao("",location.latitude,location.longitude,0)
                        enviarDadosLocalizacao(localizacao)
                        requireActivity().findViewById<TextView>(R.id.latitude).text = location.latitude.toString()
                        requireActivity().findViewById<TextView>(R.id.longitude).text = location.longitude.toString()

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
}
