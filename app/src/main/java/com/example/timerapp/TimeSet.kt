package com.example.timerapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.timer_setting.*


class TimeSet : DialogFragment(), NumberPicker.OnValueChangeListener {

    private lateinit var listener: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            this.listener = context
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement NoticeDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.timer_setting, null)!!
        val builder = AlertDialog.Builder(context)

        val hourPicker = dialogView.findViewById<NumberPicker>(R.id.hourPicker)
        hourPicker.minValue = 0
        hourPicker.maxValue = 6
        hourPicker.value = 0

        val minPicker = dialogView.findViewById<NumberPicker>(R.id.minPicker)
        minPicker.minValue = 0
        minPicker.maxValue = 59
        minPicker.value = 5

        val secPicker = dialogView.findViewById<NumberPicker>(R.id.secPicker)
        secPicker.minValue = 0
        secPicker.maxValue = 59
        secPicker.value = 0

        builder.setView(dialogView)
        builder.setTitle("Setting Timer")

        builder.setPositiveButton(
            "ok"
        ) { dialog, which ->
            val mainActivity = activity as MainActivity?

            val hour = java.lang.String.valueOf(hourPicker.getValue()).toInt()
            val min = java.lang.String.valueOf(minPicker.getValue()).toInt()
            val sec = java.lang.String.valueOf(secPicker.getValue()).toInt()

            mainActivity?.initDisplayTime(hour, min, sec)
            mainActivity?.setStartTime()
        }
        return builder.create()
    }

    override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }
}