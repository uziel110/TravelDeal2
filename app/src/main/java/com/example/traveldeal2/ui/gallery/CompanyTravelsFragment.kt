package com.example.traveldeal2.ui.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traveldeal2.R
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.utils.App
import com.example.traveldeal2.utils.Utils
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class CompanyTravelsFragment : Fragment(), CompanyRecyclerViewAdapter.CompanyCardButtonsListener {
    private lateinit var companyTravelsViewModel: CompanyTravelsViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var travelsList: MutableList<Travel?>
    var vehicleLocation: String = ""
    lateinit var vehicleLatLng: LatLng
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
        etDistance = view.findViewById(R.id.et_MaxDistance)

        placeAutoComplete()
        setEditTextListener(etDistance)

        recyclerView.layoutManager = LinearLayoutManager(context)

    }

    private fun setEditTextListener(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filter()
            }
        })
    }

    fun filter() {
        if (vehicleLocation != "" && etDistance.text.toString() != "") {
            companyTravelsViewModel.getRelevantTravels(
                etDistance.text.toString().toInt(),
                vehicleLatLng,
                requireActivity().applicationContext
            ).observe(viewLifecycleOwner, {
                travelsList = (it as List<Travel>).toMutableList()
                recyclerView.adapter =
                    CompanyRecyclerViewAdapter(it, this)
            })

        } else
            Toast.makeText(this.requireContext(), "Fill all the fields", Toast.LENGTH_SHORT).show()
    }

    private fun placeAutoComplete() {
        Places.initialize(this.requireContext(), "AIzaSyBlm-gYIse1zkWi3WwqQg3w9UOxRm4P3pE")

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
                vehicleLocation = place.address.toString()
                vehicleLatLng = place.latLng!!
                filter()
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                Toast.makeText(App.instance, "Auto complete error", Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun createCall(phoneNumber: String) {
        if (ActivityCompat.checkSelfPermission(
                App.instance, Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already available, start camera preview
            Utils.createCall(phoneNumber)
        } else {
            // Permission is missing and must be requested.
            requestCallPermission()
        }
    }

    override fun sendSms(phoneNumber: String, travel: Travel) {
        if (ContextCompat.checkSelfPermission(
                App.instance,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Utils.sendSms(phoneNumber, travel)
        } else {
            ActivityCompat.requestPermissions(
                this.requireActivity(), arrayOf(Manifest.permission.SEND_SMS),
                1
            )
        }
    }

    fun requestCallPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this.requireActivity(),
                Manifest.permission.CALL_PHONE
            )
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),
                0
            )
        } else {
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),
                0
            )
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == 0) {
//            // Request for camera permission.
//            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission has been granted. Start camera preview Activity.
//
//            } else {
//                // Permission request was denied.
//            }
//        }
//    }
}