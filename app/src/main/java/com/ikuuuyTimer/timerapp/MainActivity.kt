package com.ikuuuyTimer.timerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private var hour: Int = 0
    private var min: Int = 0
    private var sec: Int = 0

    private var isFirst: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.currentTime.observe(this, Observer { response ->
            tvTime.text = response.toString()
        })

        viewModel.finishSentence.observe(this, Observer { response ->
            tvTime.text = response.toString()
        })

        btnSelect.setOnClickListener {
            val dialog = TimeSet()
            dialog.show(supportFragmentManager, "Setting Timer")
            viewModel.isRunning = false
            if (!isFirst) viewModel.mCountDown.cancel()
            isFirst = false
        }

        btnStart.setOnClickListener {
            if (!viewModel.isRunning) viewModel.startTimer(viewModel.startTime)
        }

        btnReset.setOnClickListener {
            viewModel.resetTimer()
        }

        btnPause.setOnClickListener {
            if (viewModel.isRunning) viewModel.pauseTimer()
        }

        btnAlarmStop.setOnClickListener {
            if(viewModel.mediaPlayer != null){
                viewModel.mediaPlayer!!.stop()
                tvTime.text = "00:00:00"
            }
        }
    }

    fun initDisplayTime(hour: Int, min: Int, sec: Int) {
        tvTime.setText(String.format("%02d:%02d:%02d", hour, min, sec))
        this.hour = hour
        this.min = min
        this.sec = sec
    }

    fun setStartTime() {
        viewModel.startTime = ((hour * 60 * 60 * 1000) + (min * 60 * 1000) + (sec * 1000)).toLong()
        viewModel.fisrtTime = viewModel.startTime
    }
}