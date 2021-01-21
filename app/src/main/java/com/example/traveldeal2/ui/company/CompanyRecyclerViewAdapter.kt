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
    private var travelList: List<Travel>,
    private val listener: CompanyCardButtonsListener
) :
    RecyclerView.Adapter<CompanyRecyclerViewAdapter.ViewHolder>() {
    val userMail = encodeKey(FirebaseAuth.getInstance().currentUser?.email)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.company_travel_card, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, listPosition: Int) {
        holder.travel = travelList[listPosition]
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
            currentItem.company.contains(userMail) == true

        val passengersNum = currentItem.passengersNumber.toString()
        holder.psgNum.text =
            if (passengersNum == "1") {
                Strings.get(R.string.onePassengers)
            } else passengersNum + " ${Strings.get(R.string.passengersNumber)}"
    }

    override fun getItemCount() = travelList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var travel: Travel
        var itemID: String = ""
        var sourceAddress: TextView = this.itemView.findViewById(R.id.TextViewCustomerName)
        var destinationAddress: TextView =
            this.itemView.findViewById(R.id.TextViewTravelDays)
        var departureDate: TextView = this.itemView.findViewById(R.id.TextViewDepartureDate)
        var returnDate: TextView = this.itemView.findViewById(R.id.TextViewReturnDate)
        var psgNum: TextView = this.itemView.findViewById(R.id.TextViewPassengersNumber) as TextView
        private var btnCall: ImageButton = this.itemView.findViewById(R.id.btn_create_call)
        private var btnSms: ImageButton = this.itemView.findViewById(R.id.btn_send_sms)
        private var btnEmail: ImageButton = this.itemView.findViewById(R.id.btn_send_email)
        var switchInterested: SwitchMaterial = this.itemView.findViewById(R.id.switch_interested)
        private val expandableLayout: LinearLayout = this.itemView.findViewById(R.id.ExpandableLayout)
        private val connectionImage: ImageView = this.itemView.findViewById(R.id.image_view_connection)

        init {
            btnCall.setOnClickListener {
                listener.createCall(travel.clientPhone)
            }

            btnSms.setOnClickListener {
                listener.sendSms(travel.clientPhone, travel)
            }

            btnEmail.setOnClickListener {
                listener.sendEMail(travel)
            }

            switchInterested.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    travel.company[userMail] = false
                    travel.requestStatus = Status.RECEIVED
                } else {// not checked
                    if (travel.requestStatus == Status.RECEIVED && travel.company.size == 1)
                        travel.requestStatus = Status.SENT
                    travel.company.remove(userMail)
                }
                listener.updateTravel(travel)
            })

            connectionImage.setOnClickListener {
                expandableLayout.visibility =
                    if (expandableLayout.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }
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