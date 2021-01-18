package com.example.traveldeal2.ui.myTravels

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class MyTravelsFragment : Fragment(), MyTravelsRecyclerViewAdapter.CompanyCardButtonsListener {

    private lateinit var myTravelsViewModel: MyTravelsViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var travelsList: MutableList<Travel?>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myTravelsViewModel =
            ViewModelProvider(this).get(MyTravelsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_my_travels, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvUserTravels)


        myTravelsViewModel.getAllTravels()?.observe(viewLifecycleOwner, {
            travelsList = (it as List<Travel>).toMutableList()
            recyclerView.adapter = MyTravelsRecyclerViewAdapter(it, this)
        })

        recyclerView.layoutManager = LinearLayoutManager(context)
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

    override fun sendSms(travel: Travel) {
        if (ContextCompat.checkSelfPermission(
                App.instance,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Utils.sendSms(travel)
        } else {
            ActivityCompat.requestPermissions(
                this.requireActivity(), arrayOf(Manifest.permission.SEND_SMS),
                1
            )
        }
    }

    override fun sendEMail(travel: Travel) {
        Utils.sendEmail(travel, false)
    }

    override fun updateTravel(travel: Travel) {
        myTravelsViewModel.updateItem(travel)
    }

    private fun requestCallPermission() {
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
}