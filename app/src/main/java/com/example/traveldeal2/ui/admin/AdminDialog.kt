package com.example.traveldeal2.ui.admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.traveldeal2.R
import java.lang.ClassCastException

class AdminDialog : AppCompatDialogFragment() {
    private lateinit var editTextPassword: EditText
    private lateinit var listener: AdminDialogListener

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        // Get the layout inflater
        val inflater = requireActivity().layoutInflater;

        val view = inflater.inflate(R.layout.dialog_signin, null)
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
            ?.setTitle(R.string.admin_enter)
            // Add action buttons
            ?.setPositiveButton(R.string.enter,
                DialogInterface.OnClickListener { _, _ ->
                    val password = editTextPassword.text.toString()
                    listener.checkAdminPassword(password)
                })
            ?.setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { _, _ ->
                    dialog?.cancel()
                })

        editTextPassword = view.findViewById(R.id.password)
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AdminDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }
    }

    interface AdminDialogListener {
        fun checkAdminPassword(password: String)
    }
}