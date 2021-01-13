package com.example.traveldeal2.ui.admin

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import androidx.annotation.RequiresApi
import android.widget.TextView
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AdminFragment : Fragment(), TravelRecyclerViewAdapter.OnItemClickListener {

    private lateinit var adminViewModel: AdminViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var travelsList: MutableList<Travel?>
    lateinit var etStartDate: EditText
    lateinit var etEndDate: EditText
    lateinit var picker: DatePickerDialog

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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adminViewModel = ViewModelProvider(this).get(AdminViewModel::class.java)
        recyclerView = view.findViewById(R.id.rvUserTravels)
        etStartDate = view.findViewById(R.id.et_HistoryStartDate)
        etEndDate = view.findViewById(R.id.et_HistoryEndDate)

        createDatePicker(etStartDate, true)
        createDatePicker(etEndDate, false)

        etEndDate.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                adminViewModel.getTravelsByDate(
                    etStartDate.text.toString(),
                    etEndDate.text.toString()
                )?.observe(viewLifecycleOwner, {
                    travelsList = (it as List<Travel>).toMutableList()
                    recyclerView.adapter = TravelRecyclerViewAdapter(it, this@AdminFragment)
                })
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onItemClick(itemID: Int) {
//        val t = travelsList[itemID]
//        Toast.makeText(this, "clientId: ${t!!.clientId}", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createDatePicker(view: EditText, start:Boolean) {
        view.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)
            // date picker dialog
            picker = DatePickerDialog(
                this.requireContext(),
                { _, theYear, monthOfYear, dayOfMonth ->
                    view.setText("$dayOfMonth/${monthOfYear + 1}/$theYear")
                },
                year, month, day

            )
            if (start) {
                picker.datePicker.minDate = System.currentTimeMillis() - 1000
                picker.show()
            }
            else {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                try {
                    picker.datePicker.minDate = sdf.parse(etStartDate.text.toString()).time
                    picker.show()
                } catch (ex: ParseException) {
                    Toast.makeText(
                        this.requireContext(),
                        R.string.enter_start_date_first,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

    }
}