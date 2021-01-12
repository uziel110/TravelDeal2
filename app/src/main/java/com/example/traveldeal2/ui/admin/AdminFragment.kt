package com.example.traveldeal2.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traveldeal2.R
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.utils.TravelRecyclerViewAdapter

class AdminFragment : Fragment(),TravelRecyclerViewAdapter.OnItemClickListener {

    private lateinit var adminViewModel: AdminViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var travelsList: MutableList<Travel?>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adminViewModel =
            ViewModelProvider(this).get(AdminViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sadmin, container, false)
//        val textView: TextView = root.findViewById(R.id.text_slideshow)
//        adminViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adminViewModel = ViewModelProvider(this).get(AdminViewModel::class.java)
        recyclerView = view.findViewById(R.id.rvUserTravels)

        val finishedTravels = adminViewModel.getAllTravels() //.getTravelsByStatus("סגור")
        finishedTravels.observe(viewLifecycleOwner, {
            if (it != null) {
                recyclerView.adapter = TravelRecyclerViewAdapter(it as List<Travel>, this)
                travelsList = it as MutableList<Travel?>
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(context)
        //recyclerView.setHasFixedSize(false)
    }

    override fun onItemClick(itemID: Int) {
//        val t = travelsList[itemID]
//        Toast.makeText(this, "clientId: ${t!!.clientId}", Toast.LENGTH_SHORT).show()
    }
}


