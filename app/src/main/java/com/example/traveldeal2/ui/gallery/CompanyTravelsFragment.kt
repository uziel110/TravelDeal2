package com.example.traveldeal2.ui.gallery

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Filter
import androidx.annotation.RequiresApi
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traveldeal2.R
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.utils.App
import com.example.traveldeal2.utils.TravelRecyclerViewAdapter
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class CompanyTravelsFragment : Fragment(), TravelRecyclerViewAdapter.OnItemClickListener {
    private lateinit var companyTravelsViewModel: CompanyTravelsViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var travelsList: MutableList<Travel?>
    var vehicleLocation: String = "חיפה" //todo remove initialization
    var vehicleLatLng: LatLng = LatLng(0.0,0.0) //todo remove initialization
    lateinit var btFilter: Button
    lateinit var etDistance: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        companyTravelsViewModel =
            ViewModelProvider(this).get(CompanyTravelsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_company_travels, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvUserTravels)
//        etLocation = view.findViewById(R.id.ac_VehicleLocation)
        etDistance = view.findViewById(R.id.et_MaxDistance)
        btFilter = view.findViewById(R.id.bt_filter)
        btFilter.setOnClickListener {
            if (vehicleLocation != "" && etDistance.text.toString() != "") {
                companyTravelsViewModel.getRelevantTravels(
                    etDistance.text.toString().toInt(),
                    vehicleLatLng,
                    requireActivity().applicationContext
                ).observe(viewLifecycleOwner, {
                    travelsList = (it as List<Travel>).toMutableList()
                    recyclerView.adapter =
                        TravelRecyclerViewAdapter(it, this@CompanyTravelsFragment)
                })
                //while (t.isAlive);
                //rvOpenTravels.adapter = arrayAdapter
//            } else {
//                rvOpenTravels.adapter = OpenTravelArrayAdapter(openTravelList, viewModel)
            }
        }
        placeAutoComplete()

        recyclerView.layoutManager = LinearLayoutManager(context)
    }

//    fun btFilterOnClick(view: View){
//        if (vehicleLocation != "" && etDistance.text.toString() != "") {
//            companyTravelsViewModel.getRelevantTravels (
//                etDistance.text.toString().toInt(),
//                vehicleLatLng,
//                requireActivity().applicationContext
//            ).observe(viewLifecycleOwner, {
//                travelsList = (it as List<Travel>).toMutableList()
//                recyclerView.adapter =
//                    TravelRecyclerViewAdapter(it, this@CompanyTravelsFragment)
//            })
//        }
//    }

    override fun onItemClick(itemID: Int) {
//        val t = travelsList[itemID]
//        Toast.makeText(this, "clientId: ${t!!.clientId}", Toast.LENGTH_SHORT).show()
    }

    private fun placeAutoComplete() {
        Places.initialize(this.requireContext(), R.string.AriLazarApi.toString())

        // Initialize the AutocompleteSupportFragment.
        val vehicleLocationAutocompleteFragment =
            childFragmentManager.findFragmentById(R.id.ac_VehicleLocation)
                    as AutocompleteSupportFragment
        vehicleLocationAutocompleteFragment.setHint("הזן את מיקום הרכב")//(getString(R.string.departureAddressHint))

        // Specify the types of place data to return.
        vehicleLocationAutocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        )

        // Set up a PlaceSelectionListener to handle the response.
        vehicleLocationAutocompleteFragment.setOnPlaceSelectedListener(object :
            PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                vehicleLocation = place.name!!
                vehicleLatLng = place.latLng!!
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                Toast.makeText(App.instance, "Auto complete error", Toast.LENGTH_SHORT).show()
            }
        })

    }
}