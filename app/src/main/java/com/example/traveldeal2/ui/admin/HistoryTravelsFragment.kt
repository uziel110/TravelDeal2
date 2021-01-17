package com.example.traveldeal2.ui.admin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traveldeal2.R
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.ui.company.CompanyRecyclerViewAdapter
import com.example.traveldeal2.utils.HistoryRecyclerViewAdapter
import com.example.traveldeal2.utils.Utils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HistoryTravelsFragment : Fragment(), HistoryRecyclerViewAdapter.HistoryCardButtonsListener {

    private lateinit var historyTravelsViewModel: HistoryTravelsViewModel
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
        historyTravelsViewModel =
            ViewModelProvider(this).get(HistoryTravelsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_history_travels, container, false)
//        val textView: TextView = root.findViewById(R.id.text_slideshow)
//        adminViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        return root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvUserTravels)
        etStartDate = view.findViewById(R.id.start_date)
        etEndDate = view.findViewById(R.id.end_date)

        createDatePicker(etStartDate, true)
        createDatePicker(etEndDate, false)

        setEditTextListener(etStartDate)
        setEditTextListener(etEndDate)

        //todo apply when one of two edit text is empty
        historyTravelsViewModel.getClosedTravels()?.observe(viewLifecycleOwner, {
            travelsList = (it as List<Travel>).toMutableList()
            recyclerView.adapter =
                HistoryRecyclerViewAdapter(it, this@HistoryTravelsFragment)
        })
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setEditTextListener(editText: EditText){
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (editText.text.toString()==""){
                    historyTravelsViewModel.getClosedTravels()?.observe(viewLifecycleOwner, {
                        travelsList = (it as List<Travel>).toMutableList()
                        recyclerView.adapter =
                            HistoryRecyclerViewAdapter(it, this@HistoryTravelsFragment)
                    })
                }
            }
        })
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun createDatePicker(view: EditText, start: Boolean) {
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
                    if (etStartDate.text.toString() != "" && etEndDate.text.toString() != "") {
                        historyTravelsViewModel.getTravelsByDate(
                            etStartDate.text.toString(),
                            etEndDate.text.toString()
                        )?.observe(viewLifecycleOwner, {
                            travelsList = (it as List<Travel>).toMutableList()
                            recyclerView.adapter =
                                HistoryRecyclerViewAdapter(it, this@HistoryTravelsFragment)
                        })
                    }
                },
                year, month, day

            )
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            if (start) {
                if (etEndDate.text.toString() != "")
                    picker.datePicker.maxDate = sdf.parse(etEndDate.text.toString()).time
                picker.show()
            } else {

                try {
                    picker.datePicker.minDate = sdf.parse(etStartDate.text.toString()).time
                    picker.show()
                } catch (ex: ParseException) {
                    Toast.makeText(
                        this.requireContext(),
                        R.string.enter_start_date_first,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun sendEMail(travel: Travel) {
        Utils.sendEmail(travel, true) // TODO: 17/01/2021 need to send the company email
    }

    override fun updateTravel(travel: Travel) {
        historyTravelsViewModel.updateItem(travel)
    }
}