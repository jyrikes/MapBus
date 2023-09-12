package com.example.mapbus.ui.mapa

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mapbus.dataSource.api.ApiServiceBuilder
import com.example.mapbus.dataSource.api.Localizacao
import com.example.mapbus.databinding.FragmentMapaBinding
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapaFragment : Fragment() {

    private var _binding: FragmentMapaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map : MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentMapaBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        getInstance().load(requireActivity(), PreferenceManager.getDefaultSharedPreferences(requireContext()))



        map = binding.map
        map.setTileSource(TileSourceFactory.MAPNIK)
        val mapController = map.controller
    }
    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause()  //needed for compass, my location overlays, v6.0.0 and up
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onResume() {
        super.onResume()
        val mapController = map.controller
        mapController.setZoom(9.5)
        val startPoint = GeoPoint(-8.33105684, -36.40133927);
        mapController.setCenter(startPoint);
        mudarPosicao(map)
        map.onResume()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun mudarPosicao(mapView: MapView) {
        val call = ApiServiceBuilder.apiService.obterLocalizacao()
        call.enqueue(object : Callback<Localizacao> {
            override fun onResponse(call: Call<Localizacao>, response: Response<Localizacao>) {
                if (response.isSuccessful) {
                    val localizacao = response.body()
                    println("Localização recebida: $localizacao")
                    val latitude = localizacao?.latitude
                    val longitude = localizacao?.longitude
                    val mapController = mapView.controller
                    val startPoint = latitude?.let { longitude?.let { it1 -> GeoPoint(it, it1) } };
                    mapController.setCenter(startPoint)
                    mapController.setZoom(19)

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
}