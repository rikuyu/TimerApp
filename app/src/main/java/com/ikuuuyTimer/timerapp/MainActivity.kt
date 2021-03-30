package com.ikuuuyTimer.timerapp

import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    var hour: Int = 0
    var min: Int = 0
    var sec: Int = 0

    var isFirst: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.currentTime.observe(this, Observer { response ->
            tvTime.setText(response)
        })

        viewModel.finishSentence.observe(this, Observer { response ->
            tvTime.setText(response)
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
            viewModel.mediaPlayer!!.stop()
            tvTime.setText("00:00:00")
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