package com.example.traveldeal2.ui.company

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.CompoundButton
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.example.traveldeal2.R
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.enums.Status
import com.example.traveldeal2.utils.*
import com.example.traveldeal2.utils.Utils.Companion.encodeKey
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth

object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return App.instance.getString(stringRes, *formatArgs)
    }
}

class CompanyRecyclerViewAdapter(
    var travelList: List<Travel>,
    private val listener: CompanyCardButtonsListener
) :
    RecyclerView.Adapter<CompanyRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.company_travel_card, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, listPosition: Int) {

        val userMail = FirebaseAuth.getInstance().currentUser?.email
        val currentItem = travelList[listPosition]
        holder.itemID = currentItem.clientId
        var tmp = currentItem.departureAddress
        holder.sourceAddress.text =
            if (tmp.indexOf(",") == -1) tmp else tmp.substring(0, tmp.lastIndexOf(","))
        tmp = currentItem.destinationAddress
        holder.destinationAddress.text =
            if (tmp.indexOf(",") == -1) tmp else tmp.substring(0, tmp.lastIndexOf(","))
        holder.departureDate.text = currentItem.departureDate
        holder.returnDate.text = currentItem.returnDate
        holder.returnDate.text = currentItem.returnDate
        holder.switchInterested.isChecked =
            currentItem.company?.contains(encodeKey(userMail)) == true

//        holder.cbApproved.isChecked = if (currentItem.requestStatus == Status.RUNNING || )

        val passengersNum = currentItem.passengersNumber.toString()
        holder.psgNum.text =
            if (passengersNum == "1") {
                Strings.get(R.string.onePassengers)
            } else passengersNum + " ${Strings.get(R.string.passengersNumber)}"

//        holder.expandableLayout.visibility = if (currentItem.expandable) View.VISIBLE else View.GONE

//        holder.mainLayout.setOnClickListener {
//            travelList[listPosition].expandable = !travelList[listPosition].expandable
//            notifyItemChanged(listPosition)
//        }

        holder.btnCall.setOnClickListener {
            listener.createCall(travelList[listPosition].clientPhone)
        }

        holder.btnSms.setOnClickListener {
            listener.sendSms(travelList[listPosition].clientPhone, travelList[listPosition])
        }

        holder.btnEmail.setOnClickListener {
            listener.sendEMail(travelList[listPosition])
        }

        holder.switchInterested.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            val currTravel = travelList[listPosition]
            val companyEmail = encodeKey(userMail)
            if (isChecked) {
                if (currTravel.company == null)
                    currTravel.company = hashMapOf()
                currTravel.company?.put(companyEmail!!, false)
                currTravel.requestStatus = Status.RECEIVED
            } else {// not checked
                if (currTravel.requestStatus == Status.RECEIVED && currTravel.company?.size == 1)
                    currTravel.requestStatus = Status.SENT
                currTravel.company?.remove(companyEmail!!)
            }
            listener.updateTravel(currTravel)
            notifyDataSetChanged()
        })
    }

    override fun getItemCount() = travelList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemID: String = ""
        var sourceAddress: TextView = this.itemView.findViewById(R.id.TextViewCustomerName)
        var destinationAddress: TextView =
            this.itemView.findViewById(R.id.TextViewTravelDays)
        var departureDate: TextView = this.itemView.findViewById(R.id.TextViewDepartureDate)
        var returnDate: TextView = this.itemView.findViewById(R.id.TextViewReturnDate)
        var psgNum: TextView = this.itemView.findViewById(R.id.TextViewPassengersNumber) as TextView
        var expandableLayout: LinearLayout = this.itemView.findViewById(R.id.ExpandableLayout)
        var mainLayout: RelativeLayout = this.itemView.findViewById(R.id.cardMainLayout)
        var btnCall: ImageButton = this.itemView.findViewById(R.id.btn_create_call)
        var btnSms: ImageButton = this.itemView.findViewById(R.id.btn_send_sms)
        var btnEmail: ImageButton = this.itemView.findViewById(R.id.btn_send_email)
        var switchInterested: SwitchMaterial = this.itemView.findViewById(R.id.switch_interested)
    }

    /**
     * Requests the [android.Manifest.permission.CAMERA] permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    interface CompanyCardButtonsListener {
        fun createCall(phoneNumber: String)
        fun sendSms(phoneNumber: String, travel: Travel)
        fun sendEMail(travel: Travel)
        fun updateTravel(travel: Travel)
    }
}