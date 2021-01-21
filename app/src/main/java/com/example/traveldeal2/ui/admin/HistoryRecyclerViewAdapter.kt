package com.example.traveldeal2.ui.admin

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.example.traveldeal2.R
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.enums.Status
import com.example.traveldeal2.utils.App
import com.google.android.material.switchmaterial.SwitchMaterial
import java.text.SimpleDateFormat
import kotlin.time.ExperimentalTime

object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return App.instance.getString(stringRes, *formatArgs)
    }
}

class HistoryRecyclerViewAdapter(
    private var travelList: List<Travel>,
    private val listener: HistoryCardButtonsListener
) :
    RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_travel_card, parent, false)
        return ViewHolder(view)
    }

    @ExperimentalTime
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("RestrictedApi", "SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, listPosition: Int) {
        holder.travel = travelList[listPosition]
        val currentItem = travelList[listPosition]
        holder.itemID = currentItem.clientId
        holder.customerName.text = currentItem.clientName
        holder.switchPaid.isChecked = currentItem.requestStatus == Status.PAID

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        holder.travelDays.text = (java.util.concurrent.TimeUnit.MILLISECONDS.toDays(
            sdf.parse(currentItem.returnDate).time - sdf.parse(currentItem.departureDate).time
        ) + 1).toString()
    }

    override fun getItemCount() = travelList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var travel: Travel
        var itemID: String = ""
        var customerName: TextView = this.itemView.findViewById(R.id.TextViewCustomerName)
        var travelDays: TextView = this.itemView.findViewById(R.id.TextViewTravelDays)
        var switchPaid: SwitchMaterial = this.itemView.findViewById(R.id.switch_paid)
        private var btnEmail: ImageButton = this.itemView.findViewById(R.id.history_btn_send_email)

        // var expandableLayout: LinearLayout = this.itemView.findViewById(R.id.ExpandableLayout)

        init {
            switchPaid.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->

                if (isChecked)
                    travel.requestStatus = Status.PAID
                else // not checked
                    travel.requestStatus = Status.CLOSED

                listener.updateTravel(travel)
            })

            btnEmail.setOnClickListener {
                listener.sendEMail(travel)
            }
            switchPaid.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->

                if (isChecked)
                    travel.requestStatus = Status.PAID
                else // not checked
                    travel.requestStatus = Status.CLOSED

                listener.updateTravel(travel)
            })

            btnEmail.setOnClickListener {
                listener.sendEMail(travel)
            }
        }
    }

    interface HistoryCardButtonsListener {
        fun sendEMail(travel: Travel)
        fun updateTravel(travel: Travel)
    }
}