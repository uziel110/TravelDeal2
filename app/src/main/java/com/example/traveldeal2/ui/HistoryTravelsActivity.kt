package com.example.traveldeal2.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traveldeal2.R
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.utils.TravelRecyclerViewAdapter

class HistoryTravelsActivity : AppCompatActivity(), TravelRecyclerViewAdapter.OnItemClickListener {
    private lateinit var model: TravelViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var travelsList: MutableList<Travel?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_travels)

        model = ViewModelProvider(this).get(TravelViewModel::class.java)
        //    .also { model = it }
        recyclerView = findViewById(R.id.rvUserTravels)
        /*
        recyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(applicationContext)
        }
*/
        val v = model.getAllTravels()
        v.observe(this, {
            if (it != null) {
                recyclerView.adapter = TravelRecyclerViewAdapter(it as List<Travel>, this)
                travelsList = it as MutableList<Travel?>
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.setHasFixedSize(false)
    }

    override fun onItemClick(itemID: Int) {
        val t = travelsList[itemID]
        Toast.makeText(this, "clientId: ${t!!.clientId}", Toast.LENGTH_SHORT).show()
    }
}